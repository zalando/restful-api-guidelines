#! /bin/bash

# Generate changelog entries from git history using AI analysis
# Usage: ./changelog.sh [OPTIONS] [YEAR|DATE]
#
# By default (no arguments):
#   - Detects latest changelog entry date from chapters/changelog.adoc
#   - Generates entries for commits since that date
#   - Outputs filtered changelog entries only
#
# Options:
#   --raw          Show full AI analysis output instead of filtered entries
#   --update       Write new entries to chapters/changelog.adoc (prepend to rule-changes section)
#   --since DATE   Use explicit date (format: YYYY-MM-DD) instead of auto-detecting
#   --year YEAR    Generate for a specific year (format: YYYY)
#
# Examples:
#   ./changelog.sh                     # Auto-detect and generate since last entry
#   ./changelog.sh --raw               # Auto-detect with full output
#   ./changelog.sh --update            # Auto-detect and update the changelog file
#   ./changelog.sh --since 2025-01-01  # Generate since specific date
#   ./changelog.sh --year 2025         # Generate for entire year 2025
#   ./changelog.sh 2025 --raw          # Legacy: year as positional argument with --raw

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(dirname "$SCRIPT_DIR")"
PROMPT_FILE="$SCRIPT_DIR/changelog.prompt"
CHANGELOG_FILE="$REPO_ROOT/chapters/changelog.adoc"

# Parse arguments
MODE="auto"  # auto, year, since
PARAM=""
RAW_OUTPUT=false
UPDATE_FILE=false

# Handle legacy positional arguments first
if [[ ${#@} -gt 0 ]] && [[ "$1" =~ ^[0-9]{4}$ ]]; then
    MODE="year"
    PARAM="$1"
    shift
fi

# Parse remaining options
while [[ ${#@} -gt 0 ]]; do
    case "$1" in
        --raw)
            RAW_OUTPUT=true
            ;;
        --update)
            UPDATE_FILE=true
            ;;
        --year)
            MODE="year"
            PARAM="${2:-}"
            if [[ -z "$PARAM" ]]; then
                echo "Error: --year requires a year argument (YYYY)" >&2
                exit 1
            fi
            shift
            ;;
        --since)
            MODE="since"
            PARAM="${2:-}"
            if [[ -z "$PARAM" ]]; then
                echo "Error: --since requires a date argument (YYYY-MM-DD)" >&2
                exit 1
            fi
            shift
            ;;
        *)
            echo "Error: Unknown option: $1" >&2
            exit 1
            ;;
    esac
    shift
done

# Check dependencies
if ! command -v zllm &> /dev/null; then
    echo "Error: 'zllm' command not found. Please install zllm to use this script." >&2
    exit 1
fi

if [[ ! -f "$PROMPT_FILE" ]]; then
    echo "Error: Prompt file not found: $PROMPT_FILE" >&2
    exit 1
fi

# Determine the date range for git log
cd "$REPO_ROOT"

case "$MODE" in
    auto)
        # Extract latest date from changelog.adoc
        if [[ ! -f "$CHANGELOG_FILE" ]]; then
            echo "Error: Changelog file not found: $CHANGELOG_FILE" >&2
            exit 1
        fi

        # Look for first date entry after "== Rule Changes" section
        LATEST_DATE=$(grep -E '^\* `[0-9]{4}-[0-9]{2}-[0-9]{2}' "$CHANGELOG_FILE" | head -1 | sed -E 's/^\* `([0-9]{4}-[0-9]{2}-[0-9]{2}).*/\1/')

        if [[ -z "$LATEST_DATE" ]]; then
            echo "Error: Could not find any date entries in changelog" >&2
            exit 1
        fi

        echo "Latest changelog entry: $LATEST_DATE" >&2
        AFTER_DATE="$LATEST_DATE"
        ;;
    year)
        if ! [[ "$PARAM" =~ ^[0-9]{4}$ ]]; then
            echo "Error: Invalid year format. Expected YYYY, got: $PARAM" >&2
            exit 1
        fi
        AFTER_DATE="$PARAM-01-01"
        BEFORE_DATE="$PARAM-12-31"
        ;;
    since)
        if ! [[ "$PARAM" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]; then
            echo "Error: Invalid date format. Expected YYYY-MM-DD, got: $PARAM" >&2
            exit 1
        fi
        AFTER_DATE="$PARAM"
        ;;
esac

# Get git log with diffs
if [[ -n "${BEFORE_DATE:-}" ]]; then
    GIT_LOG=$(git log -p --after="$AFTER_DATE" --before="$BEFORE_DATE")
else
    GIT_LOG=$(git log -p --after="$AFTER_DATE")
fi

if [[ -z "$GIT_LOG" ]]; then
    echo "No commits found since $AFTER_DATE" >&2
    exit 0
fi

# Pipe prompt and git log through zllm
OUTPUT=$(cat "$PROMPT_FILE" <(echo) <(echo "$GIT_LOG") | zllm)

# Extract changelog entries (lines starting with '*')
ENTRIES=$(echo "$OUTPUT" | grep '^\*' || true)

# Output or update
if [[ "$RAW_OUTPUT" == true ]]; then
    echo "$OUTPUT"
elif [[ "$UPDATE_FILE" == true ]]; then
    if [[ -z "$ENTRIES" ]]; then
        echo "No changelog entries generated. Not updating file." >&2
        exit 0
    fi

    # Create temporary file with new entries inserted after "== Rule Changes" header
    # Split file into: header + new entries + rest
    HEADER_LINE=$(grep -n "^== Rule Changes$" "$CHANGELOG_FILE" | cut -d: -f1)
    if [[ -z "$HEADER_LINE" ]]; then
        echo "Error: '== Rule Changes' header not found in $CHANGELOG_FILE. Cannot update changelog." >&2
        exit 1
    fi

    {
        # Read file up to (but not including) "== Rule Changes"
        head -n "$((HEADER_LINE - 1))" "$CHANGELOG_FILE"
        # Add the header and blank line
        echo "== Rule Changes"
        echo
        # Add new entries
        echo "$ENTRIES"
        # Add existing entries (skip "== Rule Changes" line and the blank line after it)
        tail -n +"$((HEADER_LINE + 2))" "$CHANGELOG_FILE"
    } > "$CHANGELOG_FILE.tmp"

    mv "$CHANGELOG_FILE.tmp" "$CHANGELOG_FILE"
    echo "Updated $CHANGELOG_FILE with new entries" >&2
else
    echo "$ENTRIES"
fi

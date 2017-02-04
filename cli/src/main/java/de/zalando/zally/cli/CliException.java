package de.zalando.zally.cli;

public class CliException extends RuntimeException {
    private String title;
    private String details;
    private CliExceptionType type;

    public CliException() {
    }

    public CliException(CliExceptionType type, String title) {
        this.type = type;
        this.title = title;
    }

    public CliException(CliExceptionType type, String title, String details) {
        this.type = type;
        this.title = title;
        this.details = details;
    }

    @Override
    public String getMessage() {
        StringBuilder result = new StringBuilder();

        if (title != null && !title.isEmpty()) {
            result.append(getType());
            result.append(title);
        }

        if (details != null && !details.isEmpty()) {
            result.append("\n\n");
            result.append(details);
        }

        return result.toString();
    }

    private String getType() {
        switch (type) {
            case API:
                return "API: ";
            case CLI:
                return "Command-line Parameters: ";
            default:
                return "";
        }
    }
}

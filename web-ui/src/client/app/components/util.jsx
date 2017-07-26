export function If (props) {
  return (props.test() ? props.children : null);
}

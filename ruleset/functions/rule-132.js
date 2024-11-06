'use strict';

const ignoredHeaders = [
  'x-b3-traceid',
  'x-b3-spanid',
  'x-b3-sampled',
  'x-b3-parentspanid',
  'b3',
  'traceparent',
  'tracestate',
];

export default (targetValue) => {
  if (ignoredHeaders.includes(targetValue.toLowerCase())) {
    return [];
  }

  const regex = /^([A-Z][a-z]*)(-[A-Z0-9][a-z0-9]*)*$/g;
  const found = targetValue.match(regex);

  if (!found) {
    return [
      {
        message: `Header parameters should be Hyphenated-Pascal-Case`,
      },
    ];
  }

  return [];
};

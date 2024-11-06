export default (targetValue, { wellUnderstood }, context) => {
  const result = [];
  if (targetValue === null || typeof targetValue !== 'object') {
    return result;
  }
  for (const verb of Object.keys(targetValue)) {
    const responses = targetValue[verb].responses || {};
    if (responses === null || typeof responses !== 'object') {
      continue;
    }
    for (const code of Object.keys(responses)) {
      if (!(code in wellUnderstood)) {
        result.push({
          message: `${code} is not a well-understood HTTP status code`,
          path: [...context.path, verb, 'responses', code],
        });
        continue;
      }
      const allowedVerbs = wellUnderstood[code].map((verb) => verb.toUpperCase());
      const upperCaseVerb = verb.toUpperCase();
      if (!allowedVerbs.includes('ALL') && !allowedVerbs.includes(upperCaseVerb)) {
        result.push({
          message: `${code} is not a well-understood HTTP status code for ${upperCaseVerb}`,
          path: [...context.path, verb, 'responses', code],
        });
        continue;
      }
    }
  }
  return result;
};

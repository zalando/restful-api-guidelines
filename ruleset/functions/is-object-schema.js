'use strict';

const assertObjectSchema = (schema) => {
  if (schema.type !== 'object') {
    throw 'Schema type is not `object`';
  }
  if (schema.additionalProperties) {
    throw 'Schema is a map';
  }
};

const check = (schema) => {
  const combinedSchemas = [...(schema.anyOf || []), ...(schema.oneOf || []), ...(schema.allOf || [])];
  if (combinedSchemas.length > 0) {
    combinedSchemas.forEach(check);
  } else {
    assertObjectSchema(schema);
  }
};

export default (targetValue) => {
  try {
    check(targetValue);
  } catch (ex) {
    return [
      {
        message: ex,
      },
    ];
  }
};

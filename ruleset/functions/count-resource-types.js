'use strict';

const extractResourceTypeFromPath = (path) => {
  return path.split('/')[path.startsWith('/') ? 1 : 0];
};

export default (targetValue, { max }) => {
  const paths = Object.keys(targetValue);
  if (paths.length <= max) return [];

  const resourcesTypes = new Set(paths.map(extractResourceTypeFromPath));
  if (resourcesTypes.size <= max) return [];

  return [
    {
      message: `More than ${max} resource types found`,
    },
  ];
};

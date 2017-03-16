#!/usr/bin/env node

const fs = require('fs');
const scm = require('node-scm-source');

fs.writeFileSync('./scm-source.json', JSON.stringify(scm(), null, 4));

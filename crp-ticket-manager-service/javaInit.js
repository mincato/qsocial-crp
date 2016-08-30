"use strict";
var fs = require("fs");
var path = require('path');
var java = require("java");

java.options.push('-Xmx512m');
java.options.push('-Xms512m');

var baseDir = path.join(__dirname, "target/dependency");
var dependencies = fs.readdirSync(baseDir);

dependencies.forEach(function(dependency){
    java.classpath.push(baseDir + "/" + dependency);
})

java.classpath.push(path.join(__dirname, "target/classes"));
java.classpath.push(path.join(__dirname, "target/test-classes"));

exports.getJavaInstance = function() {
    return java;
}
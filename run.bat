@echo off
PATH %PATH%;%JAVA_HOME%\bin\
java -Darrayv.disableSynthReflect=true -cp bin;lib/classgraph-4.8.47.jar;cache main.ArrayVisualizer %*

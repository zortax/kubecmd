## kubecmd   

![Java CI with Gradle](https://github.com/zortax/kubecmd/workflows/Java%20CI%20with%20Gradle/badge.svg)

`Kubecmd` is a simple tool that allows you to send commands (or arbitrary strings) 
to the input stream of the entry point process of a running kubernetes pod. This is
useful as `kubectl attach` doesn't work well with scripts.

#### Usage

To send `say "Hello World!"` to a pod named `some-pod` that is running in the dafult
namespace, simply run the following command:
```
$ java -jar kubecmd.jar some-pod "say \"Hello World!\""
```

To reference a different namespace, use `-n` or `--namespace`:
```
$ java -jar kubecmd.jar -n some-namespace some-pod "say \"Hello World!\""
```

If you do not want to send a carriage return/new line at the end of the command, use 
`-c` or `--no-carriage`:
```
$ java -jar kubecmd.jar some-pod "say \"Someone needs to press enter :–(\"" --no-carriage
```

#### Building

To build `kubecmd`, just run the `:jar` Gradle Tak:
```
$ ./gradlew build :jar
```

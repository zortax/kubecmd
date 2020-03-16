package de.zortax.kubecmd;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

public class CLIOptions {

  @Arg(dest = "verbose")
  public boolean verbose;

  @Arg(dest = "no-carriage")
  public boolean noCarriage;

  @Arg(dest = "namespace")
  public String namespace;

  @Arg(dest = "pod")
  public String pod;

  @Arg(dest = "command")
  public String[] commands;

  public static ArgumentParser buildParser() {

    ArgumentParser parser =
        ArgumentParsers.newFor("kubecmd")
            .singleMetavar(true)
            .build()
            .defaultHelp(true)
            .description(
                "Send commands to the inputstream of a process running in a kubernetes pod.");

    parser
        .addArgument("-v", "--verbose")
        .dest("verbose")
        .action(Arguments.storeTrue())
        .help("Be verbose");

    parser
        .addArgument("-c", "--no-carriage")
        .dest("no-carriage")
        .action(Arguments.storeTrue())
        .help("Do not send a carriage return at the end of each command");

    parser.addArgument("-n", "--namespace")
            .dest("namespace")
            .setDefault("default")
            .help("Set the kubernetes namespace");

    parser.addArgument("pod")
            .dest("pod")
            .help("The pod to send the command to");

    parser
        .addArgument("command")
        .nargs("+")
        .dest("command")
        .help("Commands to send to the entry point process running in the pod");

    return parser;
  }

  public static CLIOptions parse(String[] args) {
    ArgumentParser parser = null;
    try {
      parser = buildParser();
      CLIOptions opts = new CLIOptions();
      parser.parseArgs(args, opts);
      return opts;
    } catch (ArgumentParserException e) {
      parser.handleError(e);
      System.exit(1);
    }
    System.err.println("error: Something went wrong while parsing the command line arguments.");
    System.exit(400);
    return null;
  }
}

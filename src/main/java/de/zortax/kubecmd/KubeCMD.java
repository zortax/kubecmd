package de.zortax.kubecmd;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Attach;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.util.Config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class KubeCMD {

  private CLIOptions opts;

  public static void main(String[] args) {
    // Support graal native-image
    System.setProperty("java.runtime.name", "Java(TM) SE Runtime Environment");
    (new KubeCMD(CLIOptions.parse(args))).run();
  }

  public KubeCMD(CLIOptions opts) {
    this.opts = opts;

  }

  public void run() {
    try {

      ApiClient client = Config.defaultClient();
      Configuration.setDefaultApiClient(client);

      Attach attach = new Attach();
      debug("Attaching to " + opts.pod + " (in namespace " + opts.namespace + ")...");
      final Attach.AttachResult result = attach.attach(opts.namespace, opts.pod, true);

      debug("Opening stream...");
      OutputStream outputStream = result.getStandardInputStream();
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));
      for (String cmd : opts.commands) {
        debug("Sending \"" + cmd + "\" to pod.");
        out.write(cmd);
        if (!opts.noCarriage)
          out.newLine();
        out.flush();
      }
      out.close();
      System.exit(0);
      // This results in a weird SocketTimeoutException... Idk...
      // result.close();

    } catch (IOException | ApiException e) {
      e.printStackTrace();
    }
  }

  public void debug(String message) {
    if (opts.verbose)
      System.out.println(message);
  }
}

package com.benefitj.vertxweb;

import io.vertx.core.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions());
    vertx.deployVerticle(new MainVerticle());
    // 停止
    Runtime.getRuntime().addShutdownHook(new Thread(vertx::close));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer()
//      .webSocketHandler(new Handler<ServerWebSocket>() {
//        @Override
//        public void handle(ServerWebSocket event) {
//        }
//      })
      .requestHandler(req -> {
        long start = System.currentTimeMillis();
        MultiMap headers = req.headers();
        StringBuilder sb = new StringBuilder();
        for (String name : headers.names()) {
          sb.append(name).append(" ==>: ").append(headers.getAll(name)).append("\n");
        }
        sb.append("remoteAddress").append(" ==>: ").append(req.connection().remoteAddress()).append("\n");
        sb.append("spend: ").append(System.currentTimeMillis() - start).append("\n");
        sb.append("thread: ").append(Thread.currentThread().getName()).append("\n");
        sb.append("time: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
        req.response()
          .putHeader("content-type", "text/plain")
          .end(sb.toString());
      }).listen(80, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 80");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  @Override
  public void stop() throws Exception {
    System.out.println("HTTP server stopped on port 80");
  }
}

package com.example.foraddingtoserverio.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.foraddingtoserverio.MainActivity2;
import com.example.foraddingtoserverio.data.model.LoggedInUser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static io.socket.client.Socket.EVENT_CONNECT;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private boolean b;
    private Socket mySocket;
    public Result<LoggedInUser> login(String username, String password, Context context) {

        try {

            mySocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mySocket.connect();



        mySocket.once(EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String id = mySocket.id();

                System.out.println(id);

                try {
                    mySocket.emit("login", username + " " + password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("logged");


            }
        });

        final FutureTask<Object> ft = new FutureTask<Object>(() -> {}, new Object());
        LoginDataSource lds = this;

        mySocket.on("logged", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.v("args", args.toString());
                System.out.println("============> args.toString " + args.toString());
                System.out.println("============> args[0].toString " + args[0].toString());
                System.out.println( Boolean.parseBoolean(args.toString()));
                System.out.println( Boolean.parseBoolean(args[0].toString()));


                boolean b = Boolean.parseBoolean(args[0].toString());
                lds.setLogged(b);
                ft.run();

            }
        });

        try {
            Object o = ft.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("this.b =============> " + this.b);
            // TODO: handle loggedInUser authentication Success : si l'user existe dans la base de donnée
            if (this.b) {
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username);
                Intent intent = new Intent(context, MainActivity2.class);

                context.startActivity(intent);
                return new Result.Success<>(fakeUser);
            } else {
                return new Result.Error(new IOException("Error logging in", new Exception()));

            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    private void setLogged(boolean b) {
        this.b = b;
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
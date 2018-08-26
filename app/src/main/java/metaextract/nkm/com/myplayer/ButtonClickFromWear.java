package metaextract.nkm.com.myplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;


public class ButtonClickFromWear {

    @SuppressLint("StaticFieldLeak")
    private static ButtonClickFromWear buttonClickFromWear;
    private Context context;

    private ButtonClickFromWear(Context context) {
        this.context = context;
    }

    public static synchronized ButtonClickFromWear getInstance(Context context) {
        if (buttonClickFromWear == null) {
            buttonClickFromWear = new ButtonClickFromWear(context.getApplicationContext());
        }
        return buttonClickFromWear;
    }

    public void MessageReceive(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("musicPlayer")) {
            String messageFromWear = new String(messageEvent.getData());
            Intent results = new Intent(context, MainActivity.class);
            results.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            switch (messageFromWear) {
                case "play":
                    results.putExtra("play", 1);
                    context.startActivity(results);
                    break;
                case "next":
                    results.putExtra("next", 2);
                    context.startActivity(results);
                    break;
                case "previous":
                    results.putExtra("previous", 3);
                    context.startActivity(results);
                    break;
                case "backward":
                    results.putExtra("backward", 4);
                    context.startActivity(results);
                    break;
                case "forward":
                    results.putExtra("forward", 5);
                    context.startActivity(results);
                    break;
            }
        }
    }
}
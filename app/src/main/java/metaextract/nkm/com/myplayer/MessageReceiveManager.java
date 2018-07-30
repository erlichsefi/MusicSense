package metaextract.nkm.com.myplayer;


import android.content.Context;
import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;


public class MessageReceiveManager {

    private static MessageReceiveManager MRM;
    private Context context;


    public MessageReceiveManager(Context context) {
        this.context = context;
    }

    public static synchronized MessageReceiveManager getInstance(Context context) {
        if (MRM == null) {
            MRM = new MessageReceiveManager(context.getApplicationContext());
        }
        return MRM;
    }

    public void MessageReceive(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("Player")) {
            if (new String(messageEvent.getData()).equals("play")) {
                Intent results = new Intent(context, MainActivity.class);
                // results.addFlags(     Intent.FLAG_ACTIVITY_REORDER_TO_FRONT  );
                results.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                results.putExtra("10", 1);
                context.startActivity(results);
            }
            if (new String(messageEvent.getData()).equals("backward")) {
                Intent results = new Intent(context, MainActivity.class);
                // results.addFlags(     Intent.FLAG_ACTIVITY_REORDER_TO_FRONT  );
                results.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                results.putExtra("20", 2);
                context.startActivity(results);
            }
            if (new String(messageEvent.getData()).equals("Previous")) {
                Intent results = new Intent(context, MainActivity.class);
                // results.addFlags(     Intent.FLAG_ACTIVITY_REORDER_TO_FRONT  );
                results.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                results.putExtra("30", 3);
                context.startActivity(results);
            }
            if (new String(messageEvent.getData()).equals("Next")) {
                Intent results = new Intent(context, MainActivity.class);
                // results.addFlags(     Intent.FLAG_ACTIVITY_REORDER_TO_FRONT  );
                results.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                results.putExtra("40", 4);
                context.startActivity(results);
            }
            if (new String(messageEvent.getData()).equals("Forward")) {
                Intent results = new Intent(context, MainActivity.class);
                // results.addFlags(     Intent.FLAG_ACTIVITY_REORDER_TO_FRONT  );
                results.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                results.putExtra("50", 5);
                context.startActivity(results);
            }
        }
        if (messageEvent.getPath().equals("Data_Shoe_Click")) {
        }
    }
}

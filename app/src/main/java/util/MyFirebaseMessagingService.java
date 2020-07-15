package util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import www.ethichadebe.com.loxion_beanery.LoginActivity;
import www.ethichadebe.com.loxion_beanery.MainActivity;
import www.ethichadebe.com.loxion_beanery.OrdersActivity;
import www.ethichadebe.com.loxion_beanery.R;

import static util.App.ORDER_CANCELLED;
import static util.App.READY_FOR_COLLECTION;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 * <p>
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 * <p>
 * <intent-filter>
 * <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String O_ID = "oID";
    public static final String S_ID = "sID";
    public static final String S_NAME = "sName";
    public static final String S_SMALL_PICTURE = "sSmallPicture";
    public static final String S_BIG_PICTURE = "sBigPicture";
    public static final String S_SHORT_DESCRIPT = "sShortDescrption";
    public static final String S_FULL_DESCRIPT = "sFullDescription";
    public static final String S_LATITUDE = "sLatitude";
    public static final String S_LONGITUDE = "sLongitude";
    public static final String S_AVE_TIME = "sAveTime";
    public static final String IS_ACTIVE = "isActive";
    public static final String S_STATUS = "sStatus";
    public static final String S_ADDRESS = "sAddress";
    public static final String S_OH = "sOH";
    public static final String O_PAST = "isPast";
    private static final String TAG = "MyFirebaseMsgService";
    private NotificationManagerCompat notificationManager;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        notificationManager = NotificationManagerCompat.from(this);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0 && (remoteMessage.getNotification() != null)) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            JSONObject Order = new JSONObject(remoteMessage.getData());
            Intent intent;
            Bundle bundle;
            switch (Objects.requireNonNull(remoteMessage.getNotification().getClickAction())) {
                case "MainActivity":
                    intent = new Intent(this, MainActivity.class);
                    bundle = new Bundle();

                    try {
                        String oID = Order.getString("oID");
                        bundle.putString(O_ID, oID);
                        intent.putExtras(bundle);
                        sendReadyForCollection(intent, Objects.requireNonNull(remoteMessage.getNotification().getTitle()),
                                remoteMessage.getNotification().getBody(),
                                Integer.parseInt(oID));
                    } catch (JSONException e) {
                        Log.d(TAG, "payload exception: " + e.toString());
                    }
                    break;
                case "LoginActivity":
                    intent = new Intent(this, LoginActivity.class);

                    try {
                        String sID = Order.getString("sID");
                        String topic = Order.getString("topic");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                                .addOnCompleteListener(task -> {
                                    sendOrderCancelled(intent, Objects.requireNonNull(remoteMessage.getNotification().getTitle()),
                                            remoteMessage.getNotification().getBody(),Integer.parseInt(sID));
                                    String msg = "msg_subscribed";
                                    if (!task.isSuccessful()) {
                                        msg = "msg_subscribe_failed";
                                    }
                                    Log.d(TAG, msg);
                                });
                    } catch (JSONException e) {
                        Log.d(TAG, "payload exception: " + e.toString());
                    }
                    break;
                case "OrdersActivity":
                    intent = new Intent(this, OrdersActivity.class);
                    bundle = new Bundle();

                    try {
                        String oID = Order.getString("oID");
                        bundle.putString(O_ID, oID);
                        bundle.putString(S_ID, Order.getString("sID"));
                        bundle.putString(S_STATUS, Order.getString("sStatus"));
                        bundle.putString(S_BIG_PICTURE, Order.getString("sBigPicture"));
                        bundle.putString(S_SMALL_PICTURE, Order.getString("sSmallPicture"));
                        bundle.putString(S_SHORT_DESCRIPT, Order.getString("sShortDescrption"));
                        bundle.putString(S_FULL_DESCRIPT, Order.getString("sFullDescription"));
                        bundle.putString(S_LATITUDE, Order.getString("sLatitude"));
                        bundle.putString(S_LONGITUDE, Order.getString("sLongitude"));
                        bundle.putString(IS_ACTIVE, Order.getString("isActive"));
                        bundle.putString(S_ADDRESS, Order.getString("sAddress"));
                        bundle.putString(S_OH, Order.getString("sOperatingHrs"));
                        bundle.putString(S_AVE_TIME, Order.getString("sAveTime"));
                        bundle.putString(O_PAST, Order.getString("isPast"));
                        intent.putExtras(bundle);

                        if (Order.getString("isPast").equals("false")) {
                            sendIncomingOrder(intent, Objects.requireNonNull(remoteMessage.getNotification().getTitle()),
                                    remoteMessage.getNotification().getBody(),
                                    Integer.parseInt(oID));
                        } else {
                            sendRating(intent, Objects.requireNonNull(remoteMessage.getNotification().getTitle()),
                                    remoteMessage.getNotification().getBody(),
                                    Integer.parseInt(oID));
                        }

                    } catch (JSONException e) {
                        Log.d(TAG, "payload exception: " + e.toString());
                    }
                    break;
            }
        }

    }

    private void sendIncomingOrder(Intent intent, String title, String messageBody, int oID) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, oID /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Setting sound
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notificationBuilder = new NotificationCompat.Builder(this, READY_FOR_COLLECTION)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.food)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(oID, notificationBuilder);

    }

    private void sendRating(Intent intent, String title, String messageBody, int oID) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, oID /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Setting sound
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notificationBuilder = new NotificationCompat.Builder(this, READY_FOR_COLLECTION)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.food)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(oID, notificationBuilder);

    }

    private void sendReadyForCollection(Intent intent, String title, String messageBody, int oID) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, oID /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Setting sound
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notificationBuilder = new NotificationCompat.Builder(this, READY_FOR_COLLECTION)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.food)
                .setContentTitle(title)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(oID, notificationBuilder);

    }

    private void sendOrderCancelled(Intent intent, String title, String messageBody, int oID) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, oID /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Setting sound
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notificationBuilder = new NotificationCompat.Builder(this, ORDER_CANCELLED)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.food)
                .setContentTitle(title)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(oID, notificationBuilder);

    }
}
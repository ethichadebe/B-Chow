package util;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    private static final String TAG = "App";
    public static final String INCOMING_ORDER = "incoming_order";
    public static final String READY_FOR_COLLECTION = "ready_for_collection";
    public static final String ORDER_CANCELLED = "order_cancelled";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationsChannel();
    }

    private void createNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel For incoming orders.
            NotificationChannel IncomingOrder = new NotificationChannel(INCOMING_ORDER, "Incoming order",
                    NotificationManager.IMPORTANCE_HIGH);
            IncomingOrder.setDescription("When the shop receives an incoming order");

            NotificationChannel ReadyForCollection = new NotificationChannel(READY_FOR_COLLECTION, "Ready For Collection",
                    NotificationManager.IMPORTANCE_HIGH);
            ReadyForCollection.setDescription("User gets notified to collect their order");

            NotificationChannel OrderCancelled = new NotificationChannel(ORDER_CANCELLED, "Order cancelled",
                    NotificationManager.IMPORTANCE_HIGH);
            ReadyForCollection.setDescription("User gets notified about order cancellation");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(IncomingOrder);
            manager.createNotificationChannel(ReadyForCollection);
            manager.createNotificationChannel(OrderCancelled);
        }
    }
}
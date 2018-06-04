package pas.com.mm.shoopingcart.stockdetail;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import pas.com.mm.shoopingcart.database.DBListenerCallback;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.database.model.OrderForm;

public class ProductDetailPresenter {
    private static String TAG="DB";
    FirebaseDatabase database = FirebaseDatabase.getInstance();



    public void getExisistingItemById(String productId, final String userId, final ProductDetailView view) {
        final DatabaseReference myRef = database.getReference("orders/" + userId.trim());
        Query query = myRef.orderByChild("productId").equalTo(productId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren())  view.setResult(null);
                else {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        OrderForm order = snapshot.getValue(OrderForm.class);
                        order.setKey(snapshot.getKey());
                        view.setResult(order);
                        myRef.removeEventListener(this);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }

    public void updateItem(String orderKey,int quantity,String userId) {
        DatabaseReference myRef = database.getReference("orders/" + userId.trim()+"/"+orderKey);
        myRef.child("quantity").setValue(quantity);
    }
}

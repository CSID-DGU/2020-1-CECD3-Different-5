package different.firebase;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {

    public String id;
    public String pwd;

    public User(String id, String pwd){
        this.id=id; this.pwd=pwd;
    }

    public String getId(){ return id; }
    public String getPwd(){ return pwd; }
    public void setID(String id){ this.id=id; }
    public void setPWD(String pwd){ this.pwd=pwd; }
}

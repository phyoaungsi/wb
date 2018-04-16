package pas.com.mm.shoopingcart.database.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by phyo on 08/10/2016.
 */
@IgnoreExtraProperties
public class Item extends Model implements Comparable<Item>{

    public String code;
    public String description;




    public List<String> children;


    public String key;


    public double discount;


    public String htmlDetail;



    public String type;
    public Date getTimeOfPost() {
        return timeOfPost;
    }

    public void setTimeOfPost(Date timeOfPost) {
        this.timeOfPost = timeOfPost;
    }

    public Date timeOfPost;

    public String title;
    public String imgUrl;
    public String getCode() {
        return code;
    }

    public Item() {
    }

    public Item(String code, String description, String imgUrl, Double amount) {
        this.code = code;
        this.description = description;
        this.imgUrl = imgUrl;
        this.amount = amount;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private Double amount;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getHtmlDetail() {
        return htmlDetail;
    }

    public void setHtmlDetail(String htmlDetail) {
        this.htmlDetail = htmlDetail;
    }
    // [START post_to_map]

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("amount", amount);
        result.put("discount", discount);
        result.put("code", code);
        result.put("description", description);
        result.put("imgUrl", imgUrl);

        result.put("htmlDetail", htmlDetail);
        result.put("title", title);
        result.put("imgUrl", imgUrl);
        result.put("timeOfPost",timeOfPost);
        result.put("type",type);
        result.put("key",key);
        result.put("children",children);

        return result;
    }


    public int hashCode() {
        return this.code.hashCode();
    }

    public boolean equals(Object o) {


        Item other=(Item)o;


        return other.code == code;
    }
    @Override
    public int compareTo(Item o) {
        String str1=this.code;
        String str2=o.getCode();
        int x = String.CASE_INSENSITIVE_ORDER.compare(str2, str1);
        if (x== 0) {
            x= str2.compareTo(str1);
        }
        return x;

    }
    public String getType() {
        return type;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}

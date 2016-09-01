package carter.streakly;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Carter Klein on 7/30/2016.
 */
public class Streak implements Parcelable{

    private int id;
    private String activityName;
    private String activityCategory;
    private String dateStarted;
    private int daysKept;
    private long startTime;
    private int isGoing;
    private long checkedTime;

    /*
        START CONSTRUCTORS
     */

    // Constructor with only Activity Name
    public Streak(String activityName){
        this.activityName = activityName;
        this.daysKept = 0;
    }

    // Constructor with Activity Name, Activity Category, Date Started
    public Streak(String activityName, String activityCategory){
        this.activityName = activityName;
        this.activityCategory = activityCategory;
        this.dateStarted = dateStarted;
    }

    // Constructor with Activity Name, Days Streak Kept
    public Streak(String activityName, int daysKept){
        this.activityName = activityName;
        this.daysKept = daysKept;
    }

    // Constructor with all params
    public Streak(int id, String activityName, String activityCategory, String dateStarted, int daysKept, long startTime, int isGoing, long checkedTime){
        this.id = id;
        this.activityName = activityName;
        this.activityCategory = activityCategory;
        this.dateStarted = dateStarted;
        this.daysKept = daysKept;
        this.startTime = startTime;
        this.isGoing = isGoing;
        this.checkedTime = checkedTime;
    }

    /*
        END CONSTRUCTORS
     */

    protected Streak(Parcel in) {
        activityName = in.readString();
        activityCategory = in.readString();
        dateStarted = in.readString();
        daysKept = in.readInt();
    }

    public static final Creator<Streak> CREATOR = new Creator<Streak>() {
        @Override
        public Streak createFromParcel(Parcel in) {
            return new Streak(in);
        }

        @Override
        public Streak[] newArray(int size) {
            return new Streak[size];
        }
    };

    /*
                GETTERS
             */
    public int getId(){return id;}
    public String getActivityName(){return activityName;}
    public String getActivityCategory(){return activityCategory;}
    public String getDateStarted(){return dateStarted;}
    public int getDaysKept(){return daysKept;}
    public long getStartTime(){return startTime;}
    public int getIsGoing(){return isGoing;}
    public long getCheckedTime(){return checkedTime;}

    /*
        SETTERS
     */
    public void setActivityName(String activityName){this.activityName = activityName;}
    public void setActivityCategory(String activityCategory){this.activityCategory = activityCategory;}
    public void setDateStarted(String dateStarted){this.dateStarted = dateStarted;}
    public void setDaysKept(int daysKept){this.daysKept = daysKept;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(activityName);
        parcel.writeString(activityCategory);
        parcel.writeString(dateStarted);
        parcel.writeInt(daysKept);
    }
}

package interfaces;

import java.rmi.*;
import java.io.Serializable;

public interface MappingInterface extends Remote, Serializable {

    public int getThiefID();
    public int getPosition();
    public void setPosition(int position);
    public boolean isAtGoal();
    public void isAtGoal(boolean atGoal);
    public boolean ended();
    public void ended(boolean ended);
}
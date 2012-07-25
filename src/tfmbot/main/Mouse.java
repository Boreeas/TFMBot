package tfmbot.main;

import java.io.Serializable;

/**
 * Representation of the Mouse. Incomplete
 * @author malte
 */
public class Mouse implements Serializable {
    
    public static final long serialVersionUID = 280120122313L;
    
    public String name;
    public String title;
    public String titleId;
    public String avatar;
    public String shamanColor;
    public String furColor;
    
    public String[] clothes;
   
    
    public int id;
    public int forumId;
    public int score;
    
    public boolean dead;
    public boolean hasCheese;
    
    public Mouse() {
        
    }
    
    public Mouse(String name, String idString, String dead, String score, String hasCheese, String titleId, String avatar, String[] clothes, String forumIdString, String furColor, String shamanColor) {
        
        this.name = name;
        this.titleId = titleId;
        this.title = TFMBot.titleNames.get(titleId);
        this.avatar = avatar;
        this.shamanColor = shamanColor;
        this.furColor = furColor;
        this.clothes = clothes;
        
        this.id = Integer.parseInt(idString);
        this.forumId = Integer.parseInt(idString);
        this.score = Integer.parseInt(score);
        
        this.dead = dead.equalsIgnoreCase("1");
        this.hasCheese = hasCheese.equalsIgnoreCase("1");
    }

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String[] getClothes() {
        return clothes;
    }
    
    public void setClothes(String[] clothes) {
        this.clothes = clothes;
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public void setDead(boolean dead) {
        this.dead = dead;
    }
    
    public int getForumId() {
        return forumId;
    }
    
    public void setForumId(int forumId) {
        this.forumId = forumId;
    }
    
    public String getFurColor() {
        return furColor;
    }
    
    public void setFurColor(String furColor) {
        this.furColor = furColor;
    }
    
    public boolean isHasCheese() {
        return hasCheese;
    }
    
    public void setHasCheese(boolean hasCheese) {
        this.hasCheese = hasCheese;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public String getShamanColor() {
        return shamanColor;
    }
    
    public void setShamanColor(String shamanColor) {
        this.shamanColor = shamanColor;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitleId() {
        return titleId;
    }
    
    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }
    //</editor-fold>
}

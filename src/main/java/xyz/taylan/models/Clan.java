package xyz.taylan.models;

import java.util.ArrayList;

public class Clan {

    private String clanFinalOwner;
    private String clanFinalName;
    private String clanPrefix;
    private ArrayList<String> clanMembers;
    private ArrayList<String> clanAllies;
    private boolean friendlyFire;
    private String clanHomeWorld;
    private double clanHomeX;
    private double clanHomeY;
    private double clanHomeZ;
    private int warwin;
    private float clanHomeYaw;
    private float clanHomePitch;

    public Clan(String clanOwner, String clanName) {
        clanFinalOwner = clanOwner;
        clanFinalName = clanName;
        clanPrefix = clanFinalName;
        clanMembers = new ArrayList<>();
        clanAllies = new ArrayList<>();
        friendlyFire = true;
        clanHomeWorld = null;
        warwin = 0;
    }

    public String getClanOwner() {
        return clanFinalOwner;
    }

    public String getClanFinalName() {
        return clanFinalName;
    }

    @Deprecated
    public void setClanFinalName(String newClanFinalName) {
        clanFinalName = newClanFinalName;
    }

    public String getClanPrefix() {
        return clanPrefix;
    }

    public void setClanPrefix(String newClanPrefix) {
        clanPrefix = newClanPrefix;
    }

    public ArrayList<String> getClanMembers() {
        return clanMembers;
    }

    public void setClanMembers(ArrayList<String> clanMembersList) {
        clanMembers = clanMembersList;
    }

    public void addClanMember(String clanMember) {
        clanMembers.add(clanMember);
    }

    public Boolean removeClanMember(String clanMember) {
        return clanMembers.remove(clanMember);
    }

    public ArrayList<String> getClanAllies() {
        return clanAllies;
    }

    public void addClanAlly(String ally){
        clanAllies.add(ally);
    }

    public void removeClanAlly(String allyUUID){
        clanAllies.remove(allyUUID);
    }

    public void setClanAllies(ArrayList<String> clanAlliesList) {
        clanAllies = clanAlliesList;
    }

   
    public String getClanHomeWorld() {
        return clanHomeWorld;
    }

    public void setClanHomeWorld(String clanHomeWorld) {
        this.clanHomeWorld = clanHomeWorld;
    }

    public double getClanHomeX() {
        return clanHomeX;
    }

    public void setClanHomeX(double clanHomeX) {
        this.clanHomeX = clanHomeX;
    }

    public double getClanHomeY() {
        return clanHomeY;
    }

    public void setClanHomeY(double clanHomeY) {
        this.clanHomeY = clanHomeY;
    }

    public double getClanHomeZ() {
        return clanHomeZ;
    }

    public void setClanHomeZ(double clanHomeZ) {
        this.clanHomeZ = clanHomeZ;
    }

    public float getClanHomeYaw() {
        return clanHomeYaw;
    }

    public void setClanHomeYaw(float clanHomeYaw) {
        this.clanHomeYaw = clanHomeYaw;
    }

    public float getClanHomePitch() {
        return clanHomePitch;
    }

    public void setClanHomePitch(float clanHomePitch) {
        this.clanHomePitch = clanHomePitch;
    }

	public int getWarwin() {
		return warwin;
	}

	public void setWarwin(int warwin) {
		this.warwin = warwin;
	}
}

package wood;


import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import java.awt.*;
import java.util.Arrays;

import static wood.Definitions.runTime;


@ScriptManifest(info = "Cuts to 15 wc", logo = "https://i.ibb.co/zmtpZsb/timberscape.png", version = 0, author = "BananaTown459", name = "Release-Timberscape")

public class main extends Script {

    int startExp;
    int startLvl;
    private long startTime;


    public void onStart() throws InterruptedException {
        startTime = System.currentTimeMillis();
        runTime = new Definitions.Timer(0);
        startExp = getSkills().getExperience(Skill.WOODCUTTING);
        startLvl = getSkills().getVirtualLevel(Skill.WOODCUTTING);
        LocationCheck();
    }



    @Override
    public final int onLoop() throws InterruptedException {
        if (inventory.contains("Steel axe", "Adamant axe", "Bronze axe", "Black axe", "Mithril axe", "Rune axe")) {
            upgradeaxe();
            levelchop();
        } else {
            status = "No axe found! Going to get one";
            getWalking().webWalk(Banks.DRAYNOR);
            bank.open();
            getBank().open();
            sleep(random(2000, 5000));
            upgradeaxe();
        }
        upgradeaxe();
        return 200;
    }

    public  boolean isNotAnimating() {
        return !myPlayer().isAnimating();
    }

    public boolean isNotMoving() {
        return !myPlayer().isMoving();
    }

    public  boolean moveMouseOutsideScreen() {
        return mouse.moveOutsideScreen();
    }
    int oaklogCount;

    @Override
    public void onMessage(Message m) {
        if (m.getMessage().contains("You get some oak")) {
            oaklogCount++;
        }
    }

    public void LumbyCheck() throws InterruptedException {
        if (Paths.Lumbridge.contains(myPosition()) && players.inventory.contains("Small fishing net")) {
            status = "At Lumbridge! Bank inventory!";
            getWalking().webWalk(Banks.DRAYNOR);
            status = "Walking to bank";
            if (!Banks.DRAYNOR.contains(myPosition())) {
                status = "Walking to bank";
                if (getWalking().webWalk(Banks.DRAYNOR)) {
                    new ConditionalSleep(0) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            return Banks.DRAYNOR.contains(myPosition());
                        }
                    }.sleep();
                } else {
                    log("failed to walk to bank");
                }
            } else if (!getBank().isOpen()) {
                status = "opening bank";
                if (getBank().open()) {
                    new ConditionalSleep(5000) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            return getBank().isOpen();
                        }
                    }.sleep();
                } else {
                    log("failed to open bank");
                }
                mouse.click(332, 195, false);
                bank.depositAll();
                bank.withdraw("Bronze axe", 1);
            }
        }
    }

    public void LocationCheck() throws InterruptedException {
        if  (!Paths.chopperarea.contains(myPlayer()) || Banks.DRAYNOR.contains(myPlayer())){
            status = "Walking to Draynor";
            getWalking().webWalk(Banks.DRAYNOR);
            if (!Banks.DRAYNOR.contains(myPosition())) {
                status = "Walking to bank";
                if (getWalking().webWalk(Banks.DRAYNOR)) {
                    new ConditionalSleep(0) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            return Banks.DRAYNOR.contains(myPosition());
                        }
                    }.sleep();
                } else {
                    log("failed to walk to bank");
                }
            } else if (!getBank().isOpen()) {
                status = "opening bank";
                if (getBank().open()) {
                    new ConditionalSleep(5000) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            return getBank().isOpen();
                        }
                    }.sleep();
                } else {
                    log("failed to open bank");
                }
                mouse.click(332, 195, false);
                bank.depositAll();
                bank.withdraw("Bronze axe", 1);
              }
            }
        }


    public void upgradeaxe() {
        int wcLvl = getSkills().getStatic(Skill.WOODCUTTING);
        if (inventory.contains("Steel axe", "Adamant axe", "Bronze axe", "Black axe", "Mithril axe", "Rune axe")) {
            if (wcLvl >= 1 && getBank().contains("Bronze axe") && wcLvl < 5) {
                status = "Grabbing bronze axe";
                getBank().depositAll();
                getBank().withdraw("Bronze axe", 1);
            }
            if (wcLvl >= 6 && getBank().contains("Steel axe") && wcLvl <= 20) {
                status = "Upgrading to Steel axe";
                getBank().depositAll();
                getBank().withdraw("Steel axe", 1);
            }

            if (wcLvl >= 21 && getBank().contains("Mithril axe") && wcLvl <= 30) {
                status = "Upgrading to Mithril axe";
                getBank().depositAll();
                getBank().withdraw("Mithril axe", 1);
            }

            if (wcLvl >= 31 && getBank().contains("Adamant axe")) {
                status = "Upgrading to Adamant axe";
                getBank().depositAll();
                getBank().withdraw("Adamant axe", 1);
            }

        } else {
            if (wcLvl >= 1 && getBank().contains("Bronze axe") && wcLvl < 5) {
                status = "Grabbing bronze axe";
                getBank().depositAll();
                getBank().withdraw("Bronze axe", 1);
            }
            if (wcLvl >= 6 && getBank().contains("Steel axe") && wcLvl <= 20) {
                status = "Upgrading to Steel axe";
                getBank().depositAll();
                getBank().withdraw("Steel axe", 1);
            }

            if (wcLvl >= 21 && getBank().contains("Mithril axe") && wcLvl <= 30) {
                status = "Upgrading to Mithril axe";
                getBank().depositAll();
                getBank().withdraw("Mithril axe", 1);
            }

            if (wcLvl >= 31 && getBank().contains("Adamant axe")) {
                status = "Upgrading to Adamant axe";
                getBank().depositAll();
                getBank().withdraw("Adamant axe", 1);
            }

        }
    }

    public void levelchop() throws InterruptedException {
        int startWcLevel = getSkills().getStatic(Skill.WOODCUTTING);
        if (startWcLevel < 15 && !getInventory().isFull()) {
            chop();
        } else if (startWcLevel >= 15 && !getInventory().isFull()) {
            chopOak();
        } else {
            bank();
        }
    }



    public void chop() throws InterruptedException {
        if (!Paths.chopperarea.contains(myPosition())) {
            status = "Walking to trees";
            getWalking().walkPath(Arrays.asList(Paths.pathtoTree));
        }
        RS2Object tree = getObjects().closest(Paths.chopperarea, "tree");
        if (isNotAnimating()) {
            if (tree != null && tree.interact("Chop down")) {
                status = "chopping trees";
                moveMouseOutsideScreen();
                sleep(random(1800, 2500));
                new ConditionalSleep(random(5000, 7000)) {
                    @Override
                    public boolean condition() {
                        return !myPlayer().isAnimating() && !tree.exists();
                    }
                }.sleep();
            } else {
                sleep(random(1800, 2500));
            }
        }
    }

    public void chopOak() throws InterruptedException {
        if (!Paths.chopperarea.contains(myPosition())) {
            status = "walking to oak trees";
            getWalking().walkPath(Arrays.asList(Paths.pathtoTree));
        }
        RS2Object tree = getObjects().closest(Paths.chopperarea, "Oak");
        if (isNotAnimating() && (isNotMoving())) {
            if (tree != null && tree.interact("Chop down")) {
                status = "chopping oaks";
                moveMouseOutsideScreen();
                sleep(random(1800, 2500));
                new ConditionalSleep(random(5000, 7000)) {
                    @Override
                    public boolean condition() {
                        return !myPlayer().isAnimating() && !tree.exists();
                    }
                }.sleep();
            } else {
                sleep(random(1800, 2500));
            }
        }
    }

    public void bank() throws InterruptedException {
        int startWcLevel = getSkills().getStatic(Skill.WOODCUTTING);
        status = "Walking to bank";
        if (!Banks.DRAYNOR.contains(myPosition())) {
            status = "Walking to bank";
            if (getWalking().walkPath(Arrays.asList(Paths.pathBank))) {
                new ConditionalSleep(5000) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        return Banks.DRAYNOR.contains(myPosition());
                    }
                }.sleep();
            } else {
                log("failed to walk to bank");
            }
        } else if (!getBank().isOpen()) {
            status = "opening bank";
            if (getBank().open()) {
                new ConditionalSleep(5000) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        return getBank().isOpen();
                    }
                }.sleep();
            } else {
                log("failed to open bank");
            }
        } else if (!getInventory().isEmpty()) {
            status = "Walking to bank";
            getBank().depositAllExcept("Bronze axe", "Iron axe", "Steel axe", "Mithril axe", "Adamant axe", "Rune axe");
            upgradeaxe();
        } else
            getBank().close();
        if (startWcLevel < 15 && !getInventory().isFull()) {
            status = "walking back to trees";
            chop();
        } else if (startWcLevel >= 15 && !getInventory().isFull()) {
            status = "walking back to oaks";
            chopOak();
        }
    }

    public void onExit() {
        log("Thank you for using my script. I hope you enjoy! Please report any bugs to the forum post. Runtime was ---- " + Definitions.formatTime(System.currentTimeMillis() - this.startTime));
    }

    public static final Color CHAT_BOX_COLOR = new Color(200, 180, 150);
    public static String status;

    public MouseTrail trail = new MouseTrail(0, 255, 255, 100, this);
    public MouseCursor cursor = new MouseCursor(52, 4, Color.red, this);
    @Override
    public void onPaint(final Graphics2D g) {
        trail.paint(g);
        cursor.paint(g);
        g.setColor(Color.black);
        g.fillRect(0, 336, 521, 146);
        g.setColor(CHAT_BOX_COLOR);
        g.fillRect(0, 338, 519, 142);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Logs chopped: " + oaklogCount, 12, 360);
        g.drawString("Exp gained: " + (getSkills().getExperience(Skill.WOODCUTTING) - startExp), 12, 380);
        g.drawString("Exp gained/h: " + Definitions.getPerHour(getSkills().getExperience(Skill.WOODCUTTING) - startExp), 12, 400);
        g.drawString("Lvls gained: " + (getSkills().getVirtualLevel(Skill.WOODCUTTING) - startLvl), 12, 420);
        g.drawString("Lvls gained/h: " + Definitions.getPerHour(getSkills().getVirtualLevel(Skill.WOODCUTTING) - startLvl), 12, 440);
        g.drawString("Time ran: " + Definitions.formatTime(System.currentTimeMillis() - this.startTime), 12, 460);
        g.drawString("GP/hr: " + Definitions.getPerHour(oaklogCount) * getGrandExchange().getOverallPrice(1521), 250, 360);
        g.drawString("Logs/h: " + Definitions.getPerHour(oaklogCount), 250, 395);
        g.drawString("Woodcutter made by BananaTown.", 250, 425);
        g.drawString("Currently " + status, 250, 460);
    }
}






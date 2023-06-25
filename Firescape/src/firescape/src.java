package firescape;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.GroundDecoration;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.script.Script;
import static firescape.definitions.runTime;
import org.osbot.rs07.api.ui.Message;

import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@ScriptManifest(name = "Firescape", author = "bananatown459", version = 1.0, info = "starts fires in varrock", logo = "https://i.imgur.com/EQtiATQ.png")
public class src extends Script {


    //TODO Add states to script so it doesnt walk back to reset area in middle of firemaking

    int startExp;
    private long startTime;
    int startLvl;
    public static String status;
    int fmLVL;
    Area startingPoint = new Area(3211, 3429, 3200, 3428);
    Area VBank = new Area(3179, 3448, 3190, 3433);

    //failsafe in case bot gets stuck somewhere (varock shops)
    public void onMessage(Message m) {
        if (m.getMessage().contains("You can't light a fire here")) {
            getWalking().walk(startingPoint);
        }
    }
    //make sure tiles r clear
    private Optional<Position> getEmptyPosition() {
        List<Position> allPositions = myPlayer().getArea(10).getPositions();

        // Remove any position with an object (except ground decorations, as they can be walked on)
        for (RS2Object object : getObjects().getAll()) {
            if (object instanceof GroundDecoration) {
                continue;
            }
            allPositions.removeIf(position -> object.getPosition().equals(position));
        }

        allPositions.removeIf(position -> !getMap().canReach(position));

        return allPositions.stream().min(Comparator.comparingInt(p -> myPosition().distance(p)));
    }
    //if player is standing on fire make sure next tile is good
    private boolean standingOnFire() {
        return getObjects().singleFilter(getObjects().getAll(), obj -> obj.getPosition().equals(myPosition()) && obj.getName().equals("Fire")) != null;
    }
    //makes sure player is in varrock square
    public void locationCheck() throws InterruptedException {
        //TODO make sure player is in varock square
      //  if (startingPoint.contains(myPlayer()) && inventory.contains("logs")) {
     //       lightFire();

        if (inventory.contains("Logs", "Oak logs", "Willow logs", "Maple logs", "Yew logs") && (startingPoint.contains(myPlayer()))) {
            lightFire();
            status = "lighting fires";
        } else if (VBank.contains(myPlayer())) {
            status = "Walking back to start fires";
            log("Still at the bank, return to starting point");
            getWalking().walk(startingPoint.getRandomPosition());
            lightFire();
        } else {
            lightFire();
            status = "lighting fires";
        }
        if (!inventory.contains("Logs", "Oak logs", "Willow logs", "Maple logs", "Yew logs") && (!startingPoint.contains(myPlayer()))) {
            levelChecker();
            status = "walking back to starting location";
            getWalking().walk(startingPoint.getRandomPosition());
        }
        if  (!inventory.contains("Logs", "Oak logs", "Willow logs", "Maple logs", "Yew logs")) {
            levelChecker();
        }
    }
    //lights oak logs in inventory on fire
    public void lightFire() throws InterruptedException {
        //TODO if player is in location, with correct logs and tinder box, start lighting fires.
        //todo if "you cannot light a fire here" reset location
        //todo scan ground for ashes and blacklist those tiles
        if (standingOnFire()) {
            getEmptyPosition().ifPresent(position -> {
                WalkingEvent walkingEvent = new WalkingEvent(position);
                walkingEvent.setMinDistanceThreshold(0);
                execute(walkingEvent);
            });
        } else if (!"Tinderbox".equals(getInventory().getSelectedItemName())) {
            getInventory().getItem("Tinderbox").interact("Use");
        } else if (getInventory().getItem("Logs", "Oak logs", "Willow logs", "Maple logs", "Yew logs").interact() && inventory.contains("Logs", "Oak logs", "Willow logs", "Maple logs", "Yew logs")) {
            Position playerPos = myPosition();
            firescape.sleep.sleepUntil(() -> !myPosition().equals(playerPos), 10_000, 600);
        }
    }
    //determine what logs to use based on level
    public void levelChecker() throws InterruptedException {
        if (fmLVL < 15) {
            if ((!inventory.contains("Logs"))) {
                log("Going to get logs");
                status = "going to get logs";
                if (!Banks.VARROCK_WEST.contains(myPosition())) {
                    if (getWalking().webWalk(Banks.VARROCK_WEST)) {
                        new ConditionalSleep(5000) {
                            @Override
                            public boolean condition() {
                                return Banks.VARROCK_WEST.contains(myPosition());
                            }
                        }.sleep();
                    } else {
                        log("failed to walk to bank");
                    }
                } else if (!getBank().isOpen()) {
                    if (getBank().open()) {
                        new ConditionalSleep(5000) {
                            @Override
                            public boolean condition() {
                                return getBank().isOpen();
                            }
                        }.sleep();
                    } else {
                        log("failed to open bank");
                    }
                } else
                if (!bank.contains("Logs") && (!inventory.contains("Logs"))) {
                    log("Bot ran out of logs. Stopping script.");
                    stop();
                }
                    getBank().depositAllExcept("Tinderbox");
 //                   outOfLogs();
                    getBank().withdraw("logs", 28);
                if (inventory.contains("Logs")) {
                    locationCheck();
                }
            }
        }
        if (fmLVL >= 15 && fmLVL < 30) {
                if ((!inventory.contains("Oak logs"))) {
                    log("Going to get logs");
                    status = "going to get oak logs";
                    if (!Banks.VARROCK_WEST.contains(myPosition())) {
                        if (getWalking().webWalk(Banks.VARROCK_WEST)) {
                            new ConditionalSleep(5000) {
                                @Override
                                public boolean condition() {
                                    return Banks.VARROCK_WEST.contains(myPosition());
                                }
                            }.sleep();
                        } else {
                            log("failed to walk to bank");
                        }
                    } else if (!getBank().isOpen()) {
                        if (getBank().open()) {
                            new ConditionalSleep(5000) {
                                @Override
                                public boolean condition() {
                                    return getBank().isOpen();
                                }
                            }.sleep();
                        } else {
                            log("failed to open bank");
                        }
                    } else
                    if (!bank.contains("Oak logs") && (!inventory.contains("Oak logs"))) {
                        log("Bot ran out of logs. Stopping script.");
                        stop();
                    }
                        getBank().depositAllExcept("Tinderbox");
 //                       outOfLogs();
                        getBank().withdraw("Oak logs", 28);
                    if (inventory.contains("Oak logs")) {
                        locationCheck();
                    }
                }
            }
        if (fmLVL >= 30 && fmLVL < 45) {
            if ((!inventory.contains("Willow logs"))) {
                log("Going to Willow logs");
                status = "going to get Willow logs";
                if (!Banks.VARROCK_WEST.contains(myPosition())) {
                    if (getWalking().webWalk(Banks.VARROCK_WEST)) {
                        new ConditionalSleep(5000) {
                            @Override
                            public boolean condition() {
                                return Banks.VARROCK_WEST.contains(myPosition());
                            }
                        }.sleep();
                    } else {
                        log("failed to walk to bank");
                    }
                } else if (!getBank().isOpen()) {
                    if (getBank().open()) {
                        new ConditionalSleep(5000) {
                            @Override
                            public boolean condition() {
                                return getBank().isOpen();
                            }
                        }.sleep();
                    } else {
                        log("failed to open bank");
                    }
                } else
                if (!bank.contains("Willow logs") && (!inventory.contains("Willow logs"))) {
                    log("Bot ran out of logs. Stopping script.");
                    stop();
                }
                    getBank().depositAllExcept("Tinderbox");
//                outOfLogs();
                getBank().withdraw("Willow logs", 28);
                if (inventory.contains("Willow logs")) {
                    locationCheck();
                }
            }
        }
        if (fmLVL >= 45 && fmLVL < 60) {
            if ((!inventory.contains("Maple logs"))) {
                log("Going to Maple logs");
                status = "going to get Maple logs";
                if (!Banks.VARROCK_WEST.contains(myPosition())) {
                    if (getWalking().webWalk(Banks.VARROCK_WEST)) {
                        new ConditionalSleep(5000) {
                            @Override
                            public boolean condition() {
                                return Banks.VARROCK_WEST.contains(myPosition());
                            }
                        }.sleep();
                    } else {
                        log("failed to walk to bank");
                    }
                } else if (!getBank().isOpen()) {
                    if (getBank().open()) {
                        new ConditionalSleep(5000) {
                            @Override
                            public boolean condition() {
                                return getBank().isOpen();
                            }
                        }.sleep();
                    } else {
                        log("failed to open bank");
                    }
                } else
                if (!bank.contains("Maple logs") && (!inventory.contains("Maple logs"))) {
                    log("Bot ran out of logs. Stopping script.");
                    stop();
                }
                    getBank().depositAllExcept("Tinderbox");
//                outOfLogs();
                getBank().withdraw("Maple logs", 28);
                if (inventory.contains("Maple logs")) {
                    locationCheck();
                }
            }
        //maple
        }
        if (fmLVL >= 60) {
            if ((!inventory.contains("Yew logs"))) {
                log("Going to Yew logs");
                status = "going to get Yew logs";
                if (!Banks.VARROCK_WEST.contains(myPosition())) {
                    if (getWalking().webWalk(Banks.VARROCK_WEST)) {
                        new ConditionalSleep(5000) {
                            @Override
                            public boolean condition() {
                                return Banks.VARROCK_WEST.contains(myPosition());
                            }
                        }.sleep();
                    } else {
                        log("failed to walk to bank");
                    }
                } else if (!getBank().isOpen()) {
                    if (getBank().open()) {
                        new ConditionalSleep(5000) {
                            @Override
                            public boolean condition() {
                                return getBank().isOpen();
                            }
                        }.sleep();
                    } else {
                        log("failed to open bank");
                    }
                } else
                if (!bank.contains("Yew logs") && (!inventory.contains("Yew logs"))) {
                    log("Bot ran out of logs. Stopping script.");
                    stop();
                }
                    getBank().depositAllExcept("Tinderbox");
//                outOfLogs();
                getBank().withdraw("Yew logs", 28);
                if (inventory.contains("Yew logs")) {
                    locationCheck();
                }
            }
        }
    }
    //Make sure bot has tinderbox in inventory
    public void tinderboxCheck() throws InterruptedException {
        if (!inventory.contains("Tinderbox")) {
            log("Doesnt have tinderbox, grabbing one");
            status = "Doesnt have tinderbox, grabbing one";
            if (!Banks.VARROCK_WEST.contains(myPosition())) {
                if (getWalking().webWalk(Banks.VARROCK_WEST)) {
                    new ConditionalSleep(5000) {
                        @Override
                        public boolean condition() {
                            return Banks.VARROCK_WEST.contains(myPosition());
                        }
                    }.sleep();
                } else {
                    log("failed to walk to bank");
                }
            } else if (!getBank().isOpen()) {
                if (getBank().open()) {
                    new ConditionalSleep(5000) {
                        @Override
                        public boolean condition() {
                            return getBank().isOpen();
                        }
                    }.sleep();
                } else {
                    log("failed to open bank");
                }
            } else {
                getBank().withdraw("Tinderbox", 1);
                getBank().close();
            }
        }
        if (inventory.contains("Tinderbox")) {
            levelChecker();
        }
    }
    //If inventory and bank are out of logs, exit bot.

        @Override
    public void onStart() throws InterruptedException {
        startTime = System.currentTimeMillis();
        runTime = new definitions.Timer(0);
        startExp = getSkills().getExperience(Skill.FIREMAKING);
        startLvl = getSkills().getVirtualLevel(Skill.FIREMAKING);
        getExperienceTracker().start(Skill.FIREMAKING);
        fmLVL = getSkills().getVirtualLevel(Skill.FIREMAKING);
        tinderboxCheck();
    }

    @Override
    public void onExit() {
        //Code here will execute after the script ends
    }

    @Override
    public int onLoop() throws InterruptedException {
        tinderboxCheck();
        if (inventory.contains("Logs", "Oak logs", "Willow logs", "Maple logs", "Yew logs") && (inventory.contains("Tinderbox"))) {
            locationCheck();
        } else {
            levelChecker();
        }

        return 100; //The amount of time in milliseconds before the loop starts over
    }
    public static final Color CHAT_BOX_COLOR = new Color(200, 180, 150);

    @Override
    public void onPaint(Graphics2D g) {
        //todo add path drawing option
        g.draw(getEmptyPosition().get().getPolygon(getBot()));

        g.setColor(Color.black);
        g.fillRect(0, 336, 521, 146);
        g.setColor(CHAT_BOX_COLOR);
        g.fillRect(0, 338, 519, 142);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
//        g.drawString("Logs burned: " + oaklogCount, 12, 360);
        g.drawString("Exp gained: " + (getSkills().getExperience(Skill.FIREMAKING) - startExp), 12, 380);
        g.drawString("Exp gained/h: " + getExperienceTracker().getGainedXPPerHour(Skill.FIREMAKING), 12, 400);
        g.drawString("Lvls gained: " + (getSkills().getVirtualLevel(Skill.FIREMAKING) - startLvl), 12, 420);
        g.drawString("Lvls gained/h: " + definitions.getPerHour(getSkills().getVirtualLevel(Skill.FIREMAKING) - startLvl), 12, 440);
        g.drawString("Time ran: " + definitions.formatTime(System.currentTimeMillis() - this.startTime), 12, 460);
//        g.drawString("GP/hr: " + definitions.getPerHour(oaklogCount) * getGrandExchange().getOverallPrice(1521), 250, 360);
//        g.drawString("Logs/h: " + definitions.getPerHour(oaklogCount), 250, 395);
        g.drawString("Firemaker made by BananaTown.", 250, 425);
        g.drawString("Currently " + status, 250, 460);
    }

}
package wood;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;

public class Paths {
    static Position[] pathtoTree = {
            new Position(3093, 3243, 0),
            new Position(3093, 3248, 0),
            new Position(3097, 3249, 0),
            new Position(3099, 3244, 0),
            new Position(3098, 3237, 0),
            new Position(3098, 3233, 0),
            new Position(3101, 3225, 0),
            new Position(3101, 3219, 0),
            new Position(3100, 3214, 0),
            new Position(3103, 3210, 0),
            new Position(3109, 3211, 0),
            new Position(3113, 3211, 0),
            new Position(3120, 3209, 0),
            new Position(3122, 3212, 0),
            new Position(3125, 3216, 0),
            new Position(3130, 3218, 0)
    };

    static Position[] pathBank = {
            new Position(3127, 3217, 0),
            new Position(3124, 3215, 0),
            new Position(3120, 3211, 0),
            new Position(3117, 3210, 0),
            new Position(3113, 3210, 0),
            new Position(3109, 3211, 0),
            new Position(3104, 3210, 0),
            new Position(3101, 3212, 0),
            new Position(3100, 3217, 0),
            new Position(3101, 3221, 0),
            new Position(3100, 3229, 0),
            new Position(3100, 3233, 0),
            new Position(3099, 3237, 0),
            new Position(3098, 3243, 0),
            new Position(3092, 3244, 0)
    };
    static Area chopperarea = new Area(
            new int[][]{
                    {3137, 3207},
                    {3138, 3212},
                    {3138, 3216},
                    {3135, 3217},
                    {3132, 3219},
                    {3126, 3219},
                    {3122, 3219},
                    {3122, 3211},
                    {3122, 3207},
                    {3127, 3204},
                    {3134, 3203}
            });
    static Area Lumbridge = new Area(3236, 3235, 3217, 3210);
}

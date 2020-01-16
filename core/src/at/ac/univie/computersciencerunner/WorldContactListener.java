package at.ac.univie.computersciencerunner;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import at.ac.univie.computersciencerunner.mapObjects.Brick;
import at.ac.univie.computersciencerunner.mapObjects.Coin;
import at.ac.univie.computersciencerunner.mapObjects.CoinBrick;
import at.ac.univie.computersciencerunner.mapObjects.ECTS;
import at.ac.univie.computersciencerunner.mapObjects.ECTSBrick;
import at.ac.univie.computersciencerunner.mapObjects.Heart;
import at.ac.univie.computersciencerunner.mapObjects.HeartBrick;
import at.ac.univie.computersciencerunner.mapObjects.HorizontalPlatform;
import at.ac.univie.computersciencerunner.mapObjects.InfoBrick;
import at.ac.univie.computersciencerunner.mapObjects.InteractiveObject;
import at.ac.univie.computersciencerunner.mapObjects.Trampoline;
import at.ac.univie.computersciencerunner.mapObjects.VerticalPlatform;
import at.ac.univie.computersciencerunner.mobs.Spear;
import at.ac.univie.computersciencerunner.mobs.SpearBug;
import at.ac.univie.computersciencerunner.mobs.WalkingBug;
import at.ac.univie.computersciencerunner.mobs.FlyingBug;
import at.ac.univie.computersciencerunner.mobs.JumpingBug;
import at.ac.univie.computersciencerunner.mobs.Player;

public class WorldContactListener implements ContactListener {

    private ComputerScienceRunner game;

    public WorldContactListener(ComputerScienceRunner game) {
        this.game = game;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int orCategoryBits = fixA.getFilterData().categoryBits + fixB.getFilterData().categoryBits;
        Player player =  ComputerScienceRunner.playScreen.getPlayer();

        if ((fixA.getUserData() == "feet" || fixB.getUserData() == "feet") && player.body.getLinearVelocity().y <= 0.0f) {
            ComputerScienceRunner.playScreen.getPlayer().setGrounded(true);
            ComputerScienceRunner.playScreen.getPlayer().setTimestampUngrounded(0);
            ComputerScienceRunner.playScreen.getPlayer().setBrickDestroyed(false);
        }

        if(orCategoryBits == ComputerScienceRunner.COLLECTIBLE_BIT + ComputerScienceRunner.PLAYER_BIT) {
            InteractiveObject interactiveObject = null;
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.COLLECTIBLE_BIT) {
                interactiveObject = ((InteractiveObject)fixA.getUserData());
            } else {
                interactiveObject = ((InteractiveObject)fixB.getUserData());
            }

            if(interactiveObject instanceof ECTS) {
                ((ECTS) interactiveObject).setToDestroy();
            }


            if(interactiveObject instanceof ECTS) {
                ((ECTS) interactiveObject).setToDestroy();
            }

            if(interactiveObject instanceof Coin) {
                ((Coin) interactiveObject).setToDestroy();
            }

            if(interactiveObject instanceof Heart) {
                ((Heart) interactiveObject).setToDestroy();
            }

        }


        if(orCategoryBits == ComputerScienceRunner.BRICK_BIT + ComputerScienceRunner.PLAYER_HEAD_BIT) {
            InteractiveObject interactiveObject = null;
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BRICK_BIT) {
                interactiveObject = ((InteractiveObject)fixA.getUserData());
            } else {
                interactiveObject = ((InteractiveObject)fixB.getUserData());
            }

            if(interactiveObject instanceof Brick) {
                ((Brick) interactiveObject).setToDestroy();
                ComputerScienceRunner.playScreen.getPlayer().setBrickDestroyed(true);
            }

            if(interactiveObject instanceof ECTSBrick) {
                ((ECTSBrick) interactiveObject).setToDestroy();
                ComputerScienceRunner.playScreen.getPlayer().setBrickDestroyed(true);
            }

            if(interactiveObject instanceof HeartBrick) {
                ((HeartBrick) interactiveObject).setToDestroy();
                ComputerScienceRunner.playScreen.getPlayer().setBrickDestroyed(true);
            }

            if(interactiveObject instanceof InfoBrick) {
                ComputerScienceRunner.playScreen.getInfoWidget().setCurrentlyDisplayed(true);
                ComputerScienceRunner.playScreen.pause();
                ComputerScienceRunner.playScreen.getCustomOrthogonalTiledMapRenderer().setAnimate(false);
                ((InfoBrick)interactiveObject).displayInfo();
                System.out.println("2135464868");
            }

            if(interactiveObject instanceof CoinBrick) {
                ((CoinBrick) interactiveObject).setToDestroy();
                ComputerScienceRunner.playScreen.getPlayer().setBrickDestroyed(true);
            }

        }

        if(orCategoryBits == ComputerScienceRunner.GOAL_BIT + ComputerScienceRunner.PLAYER_BIT) {
            ComputerScienceRunner.playScreen.getPlayer().setReachedGoal(true);
        }

        if(orCategoryBits == ComputerScienceRunner.BUG_LEFT_SENSOR_BIT + ComputerScienceRunner.GROUND_BIT || orCategoryBits == ComputerScienceRunner.BUG_LEFT_SENSOR_BIT + ComputerScienceRunner.ONEWAY_PLATFORM_BIT) {
            Object enemy = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BUG_LEFT_SENSOR_BIT) {
                enemy = fixA.getUserData();
            } else {
                enemy = fixB.getUserData();
            }
            if(enemy instanceof WalkingBug) {
                ((WalkingBug) enemy).setLeftSensorCollides(true);
            }
        }

        if(orCategoryBits == ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT + ComputerScienceRunner.GROUND_BIT || orCategoryBits == ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT + ComputerScienceRunner.ONEWAY_PLATFORM_BIT) {

            Object enemy = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT) {
                enemy = fixA.getUserData();
            } else {
                enemy = fixB.getUserData();
            }
            if(enemy instanceof WalkingBug) {
                ((WalkingBug) enemy).setRightSensorCollides(true);
            }
        }

        if(orCategoryBits == ComputerScienceRunner.PLAYER_FEET_BIT + ComputerScienceRunner.BUG_HEAD_BIT) {
            Object enemy = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BUG_HEAD_BIT) {
                enemy = fixA.getUserData();
            } else {
                enemy = fixB.getUserData();
            }
            if(enemy instanceof WalkingBug) {
                ((WalkingBug) enemy).hitOnHead();
            }

            if(enemy instanceof FlyingBug) {
                ((FlyingBug) enemy).hitOnHead();
            }

            if(enemy instanceof SpearBug) {
                ((SpearBug) enemy).hitOnHead();
            }

            if(enemy instanceof Spear) {
                ((Spear) enemy).hitOnHead();
            }

            if(enemy instanceof JumpingBug) {
                if(!((JumpingBug) enemy).isJumping()) {
                    ((JumpingBug) enemy).hitOnHead();
                } else {
                    player.setHearts(player.getHearts()-1);
                    ComputerScienceRunner.playScreen.getHud().setHeartsCount(player.getHearts());
                    player.setBlinking(true);
                    player.setBlinkingStartTimestamp(System.currentTimeMillis());
                    player.body.setLinearVelocity(0, 0);
                    player.body.applyLinearImpulse(new Vector2(50f, 0), player.body.getWorldCenter(), true);
                    if (player.getHearts() == 0) {
                        player.setCurrentAnimation(player.getDeathAnimation());
                    }
                }
            }
        }

        if(orCategoryBits == ComputerScienceRunner.PLAYER_BIT + ComputerScienceRunner.BUG_BODY_BIT) {
            player.setTouchingEnemy(true);

            Object enemy = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BUG_BODY_BIT) {
                enemy = fixA.getUserData();
            } else {
                enemy = fixB.getUserData();
            }

            if(enemy instanceof Spear) {
                ((Spear) enemy).hitOnHead();
            }
        }

        if(orCategoryBits == ComputerScienceRunner.PLAYER_BIT + ComputerScienceRunner.BUG_LEFT_SENSOR_BIT) {
            Object enemy = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BUG_LEFT_SENSOR_BIT) {
                enemy = fixA.getUserData();
            } else {
                enemy = fixB.getUserData();
            }
            if(enemy instanceof SpearBug) {
                ((SpearBug) enemy).setThrowLeft(true);
                ((SpearBug) enemy).setStateTime(0);
            }
        }

        if(orCategoryBits == ComputerScienceRunner.PLAYER_BIT + ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT) {
            Object enemy = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT) {
                enemy = fixA.getUserData();
            } else {
                enemy = fixB.getUserData();
            }
            if(enemy instanceof SpearBug) {
                ((SpearBug) enemy).setThrowRight(true);
                ((SpearBug) enemy).setStateTime(0);
            }
        }

        if((orCategoryBits == ComputerScienceRunner.PLAYER_FEET_BIT + ComputerScienceRunner.SPIKES_BIT || orCategoryBits == ComputerScienceRunner.PLAYER_HEAD_BIT + ComputerScienceRunner.SPIKES_BIT)  && !player.isBlinking()) {
            player.setHearts(player.getHearts()-1);
            ComputerScienceRunner.playScreen.getHud().setHeartsCount(player.getHearts());
            player.setBlinking(true);
            player.setBlinkingStartTimestamp(System.currentTimeMillis());
            player.body.setLinearVelocity(0, 0);
            player.body.applyLinearImpulse(new Vector2(0f, 2f), player.body.getWorldCenter(), true);
            if (player.getHearts() == 0) {
                player.setCurrentAnimation(player.getDeathAnimation());
            }
        }

        if(orCategoryBits == ComputerScienceRunner.PLAYER_BIT + ComputerScienceRunner.TRAMPOLINE_BIT && !player.isGrounded()) {
            player.setTrampolineJump(true);
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.TRAMPOLINE_BIT) {
                ((Trampoline)fixA.getUserData()).setPressDown(true);
            } else {
                ((Trampoline)fixB.getUserData()).setPressDown(true);
            }
        }


        if(orCategoryBits == ComputerScienceRunner.PLAYER_FEET_BIT + ComputerScienceRunner.ONEWAY_PLATFORM_BIT) {
            Object platform = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.ONEWAY_PLATFORM_BIT) {
                platform = fixA.getUserData();
            } else {
                platform = fixB.getUserData();
            }

            if(platform instanceof HorizontalPlatform) {
                HorizontalPlatform horizontalPlatform = (HorizontalPlatform) platform;
                horizontalPlatform.setPlayerContact(true);
                player.setOnMovingHorizontalPlatform(true);
            }

            if(platform instanceof VerticalPlatform) {
                VerticalPlatform verticalPlatform = (VerticalPlatform) platform;
                verticalPlatform.setPlayerContact(true);
                player.setOnMovingVerticalPlatform(true);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        Player player =  ComputerScienceRunner.playScreen.getPlayer();

        int orCategoryBits = fixA.getFilterData().categoryBits + fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == "feet" || fixB.getUserData() == "feet") {
                ComputerScienceRunner.playScreen.getPlayer().setGrounded(false);
                ComputerScienceRunner.playScreen.getPlayer().setTimestampUngrounded(System.currentTimeMillis());
        }

        if(orCategoryBits == ComputerScienceRunner.BUG_LEFT_SENSOR_BIT + ComputerScienceRunner.GROUND_BIT || orCategoryBits == ComputerScienceRunner.BUG_LEFT_SENSOR_BIT + ComputerScienceRunner.ONEWAY_PLATFORM_BIT) {
            Object enemy = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BUG_LEFT_SENSOR_BIT) {
                enemy = fixA.getUserData();
            } else {
                enemy = fixB.getUserData();
            }
            if(enemy instanceof WalkingBug) {
                ((WalkingBug) enemy).setLeftSensorCollides(false);
            }
        }

        if(orCategoryBits == ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT + ComputerScienceRunner.GROUND_BIT || orCategoryBits == ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT + ComputerScienceRunner.ONEWAY_PLATFORM_BIT) {
            Object enemy = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT) {
                enemy = fixA.getUserData();
            } else {
                enemy = fixB.getUserData();
            }
            if(enemy instanceof WalkingBug) {
                ((WalkingBug) enemy).setRightSensorCollides(false);
            }
        }

        if(orCategoryBits == ComputerScienceRunner.PLAYER_BIT + ComputerScienceRunner.BUG_BODY_BIT) {
            player.setTouchingEnemy(false);
        }

        if(orCategoryBits == ComputerScienceRunner.PLAYER_BIT + ComputerScienceRunner.BUG_LEFT_SENSOR_BIT) {
            Object enemy = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BUG_LEFT_SENSOR_BIT) {
                enemy = fixA.getUserData();
            } else {
                enemy = fixB.getUserData();
            }
            if(enemy instanceof SpearBug) {
                ((SpearBug) enemy).setThrowLeft(false);
                ((SpearBug) enemy).setCurrentAnimation(((SpearBug) enemy).getStandLeftAnimation());
                ((SpearBug) enemy).setStateTime(0);
            }
        }

        if(orCategoryBits == ComputerScienceRunner.PLAYER_BIT + ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT) {
            Object enemy = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT) {
                enemy = fixA.getUserData();
            } else {
                enemy = fixB.getUserData();
            }
            if(enemy instanceof SpearBug) {
                ((SpearBug) enemy).setThrowRight(false);
                ((SpearBug) enemy).setCurrentAnimation(((SpearBug) enemy).getStandRightAnimation());
                ((SpearBug) enemy).setStateTime(0);
            }
        }

        if(orCategoryBits == ComputerScienceRunner.PLAYER_BIT + ComputerScienceRunner.TRAMPOLINE_BIT) {
            player.setTrampolineJump(false);
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.TRAMPOLINE_BIT) {
                ((Trampoline)fixA.getUserData()).setPressDown(false);
            } else {
                ((Trampoline)fixB.getUserData()).setPressDown(false);
            }
        }

        if(orCategoryBits == ComputerScienceRunner.PLAYER_FEET_BIT + ComputerScienceRunner.ONEWAY_PLATFORM_BIT) {
            Object platform = null;

            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.ONEWAY_PLATFORM_BIT) {
                platform = fixA.getUserData();
            } else {
                platform = fixB.getUserData();
            }

            if(platform instanceof HorizontalPlatform) {
                HorizontalPlatform horizontalPlatform = (HorizontalPlatform) platform;
                horizontalPlatform.setPlayerContact(false);
                player.setOnMovingHorizontalPlatform(false);
            }

            if(platform instanceof VerticalPlatform) {
                VerticalPlatform verticalPlatform = (VerticalPlatform) platform;
                verticalPlatform.setPlayerContact(false);
                player.setOnMovingVerticalPlatform(false);
            }
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int orCategoryBits = fixA.getFilterData().categoryBits + fixB.getFilterData().categoryBits;

        if(orCategoryBits == ComputerScienceRunner.ONEWAY_PLATFORM_BIT + ComputerScienceRunner.PLAYER_FEET_BIT) {
            if (ComputerScienceRunner.playScreen.getPlayer().body.getLinearVelocity().y > 0) {
                contact.setEnabled(false);
            }
        }

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}

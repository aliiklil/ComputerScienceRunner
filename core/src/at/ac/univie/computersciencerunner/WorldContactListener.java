package at.ac.univie.computersciencerunner;

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
import at.ac.univie.computersciencerunner.mapObjects.InfoBrick;

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

        if(fixA.getUserData() == "feet" || fixB.getUserData() == "feet") {
            ComputerScienceRunner.playScreen.getPlayer().setGrounded(true);
        }





        if(orCategoryBits == ComputerScienceRunner.BRICK_BIT + ComputerScienceRunner.PLAYER_HEAD_BIT) {
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.BRICK_BIT) {
                ((Brick)fixA.getUserData()).setToDestroy();
            } else {
                ((Brick)fixB.getUserData()).setToDestroy();
            }
        }





        if(orCategoryBits == ComputerScienceRunner.ECTS_BIT + ComputerScienceRunner.PLAYER_BIT) {
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.ECTS_BIT) {
                ((ECTS)fixA.getUserData()).setToDestroy();
            } else {
                ((ECTS)fixB.getUserData()).setToDestroy();
            }
        }

        if(orCategoryBits == ComputerScienceRunner.ECTS_BRICK_BIT + ComputerScienceRunner.PLAYER_HEAD_BIT) {
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.ECTS_BRICK_BIT) {
                ((ECTSBrick)fixA.getUserData()).setToDestroy();
            } else {
                ((ECTSBrick)fixB.getUserData()).setToDestroy();
            }
        }



        if(orCategoryBits == ComputerScienceRunner.HEART_BIT + ComputerScienceRunner.PLAYER_BIT) {
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.HEART_BIT) {
                ((Heart)fixA.getUserData()).setToDestroy();
            } else {
                ((Heart)fixB.getUserData()).setToDestroy();
            }
        }

        if(orCategoryBits == ComputerScienceRunner.HEART_BRICK_BIT + ComputerScienceRunner.PLAYER_HEAD_BIT) {
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.HEART_BRICK_BIT) {
                ((HeartBrick)fixA.getUserData()).setToDestroy();
            } else {
                ((HeartBrick)fixB.getUserData()).setToDestroy();
            }
        }

        if(orCategoryBits == ComputerScienceRunner.INFO_BRICK_BIT + ComputerScienceRunner.PLAYER_HEAD_BIT) {
                ComputerScienceRunner.playScreen.getInfoWidget().setCurrentlyDisplayed(true);
                ComputerScienceRunner.playScreen.pause();
                ComputerScienceRunner.playScreen.getCustomOrthogonalTiledMapRenderer().setAnimate(false);
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.INFO_BRICK_BIT) {
                ((InfoBrick)fixA.getUserData()).displayInfo();
            } else {
                ((InfoBrick)fixB.getUserData()).displayInfo();
            }
        }

        if(orCategoryBits == ComputerScienceRunner.COIN_BIT + ComputerScienceRunner.PLAYER_BIT) {
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.COIN_BIT) {
                ((Coin)fixA.getUserData()).setToDestroy();
            } else {
                ((Coin)fixB.getUserData()).setToDestroy();
            }
        }

        if(orCategoryBits == ComputerScienceRunner.COIN_BRICK_BIT + ComputerScienceRunner.PLAYER_HEAD_BIT) {
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.COIN_BRICK_BIT) {
                ((CoinBrick)fixA.getUserData()).setToDestroy();
            } else {
                ((CoinBrick)fixB.getUserData()).setToDestroy();
            }
        }

        if(orCategoryBits == ComputerScienceRunner.GOAL_BIT + ComputerScienceRunner.PLAYER_BIT) {
            ComputerScienceRunner.playScreen.getPlayer().setReachedGoal(true);
        }

    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "feet" || fixB.getUserData() == "feet") {
            ComputerScienceRunner.playScreen.getPlayer().setGrounded(false);
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}

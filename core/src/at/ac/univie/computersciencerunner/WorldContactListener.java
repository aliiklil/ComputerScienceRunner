package at.ac.univie.computersciencerunner;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import at.ac.univie.computersciencerunner.mapObjects.Brick;
import at.ac.univie.computersciencerunner.mapObjects.ECTS;
import at.ac.univie.computersciencerunner.mapObjects.ECTSBrick;
import at.ac.univie.computersciencerunner.mapObjects.Heart;
import at.ac.univie.computersciencerunner.mapObjects.HeartBrick;
import at.ac.univie.computersciencerunner.mapObjects.InfoBrick;

public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int orCategoryBits = fixA.getFilterData().categoryBits + fixB.getFilterData().categoryBits;

        if(fixA.getUserData() == "feet" || fixB.getUserData() == "feet") {
            ComputerScienceRunner.playScreen.getPlayer().setGrounded(true);
            System.out.println("TRUE");
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
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.INFO_BRICK_BIT) {
                ComputerScienceRunner.playScreen.getInfoWidget().setCurrentlyDisplayed(true);
            } else {
                ComputerScienceRunner.playScreen.getInfoWidget().setCurrentlyDisplayed(true);
            }
        }

    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "feet" || fixB.getUserData() == "feet") {
            ComputerScienceRunner.playScreen.getPlayer().setGrounded(false);
            System.out.println("FALSE");
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}

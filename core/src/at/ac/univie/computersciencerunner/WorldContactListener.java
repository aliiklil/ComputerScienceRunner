package at.ac.univie.computersciencerunner;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import at.ac.univie.computersciencerunner.mapObjects.ECTS;

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




        if(orCategoryBits == ComputerScienceRunner.ECTS_BIT + ComputerScienceRunner.PLAYER_BIT) {
            if (fixA.getFilterData().categoryBits == ComputerScienceRunner.ECTS_BIT) {
                ((ECTS)fixA.getUserData()).setToDestroy();
            } else {
                ((ECTS)fixB.getUserData()).setToDestroy();
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

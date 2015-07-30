package com.lemming.game.trash;

import com.badlogic.gdx.physics.box2d.*;
import com.lemming.game.comps.UnitComp;

/**
 * Created by Alexander on 25.07.2015.
 */
public class PhysicsContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData() instanceof UnitComp){
            ((UnitComp) fixtureA.getUserData()).touched(contact);
        }
        if(fixtureB.getUserData() instanceof UnitComp){
            ((UnitComp) fixtureB.getUserData()).touched(contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData() instanceof UnitComp){
            ((UnitComp) fixtureA.getUserData()).untouched(contact);
        }
        if(fixtureB.getUserData() instanceof UnitComp){
            ((UnitComp) fixtureB.getUserData()).untouched(contact);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

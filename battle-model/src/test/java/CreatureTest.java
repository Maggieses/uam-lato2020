import com.google.common.collect.Range;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CreatureTest {

    private final static int NOT_IMPORTANT_1 = 1;
    private final static Range<Integer> NOT_IMPORTANT_RANGE = Range.closed(1, 1);
    private final static int NOT_IMPORTANT_0 = 0;

    @Test
    void creatureOneShouldLostTwoHp() {
        Creature c1 = new Creature(10, NOT_IMPORTANT_RANGE, NOT_IMPORTANT_0);
        Creature c2 = new Creature(NOT_IMPORTANT_0, Range.closed(2, 2), NOT_IMPORTANT_0);

        c2.attack(c1);

        assertEquals(8, c1.getHp());
    }

    @Test
    void creatureShouldLostOneHpBecauseHasOneDefence() {
        Creature defender = new Creature(10, NOT_IMPORTANT_RANGE, 1);
        Creature attacker = new Creature(NOT_IMPORTANT_1, Range.closed(2, 2), NOT_IMPORTANT_1);

        attacker.attack(defender);

        assertEquals(9, defender.getHp());
    }

    @Test
    void creatureShouldNotHealAfterDefendWeakerCreature() {
        Creature defender = Creature.builder().aMaxHp(10).aAttack(NOT_IMPORTANT_RANGE).aArmor(100).build();
        Creature attacker = new Creature(NOT_IMPORTANT_1, Range.closed(50, 50), NOT_IMPORTANT_1);

        attacker.attack(defender);

        assertEquals(9, defender.getHp());
    }

    @Test
    void creatureShouldCounterAttack() {
        Creature defender = Creature.builder().aMaxHp(10).aAttack(Range.closed(2, 2)).aArmor(1).build();
        Creature attacker = Creature.builder().aMaxHp(10).aAttack(Range.closed(2, 2)).aArmor(1).build();

        attacker.attack(defender);

        assertEquals(9, attacker.getHp());
    }

    @Test
    void defenderCreatureShouldCounterAttackOnlyOncePerTurn() {
        Creature defender = Creature.builder().aMaxHp(10).aAttack(Range.closed(2, 2)).aArmor(1).build();
        Creature attacker = Creature.builder().aMaxHp(10).aAttack(Range.closed(2, 2)).aArmor(1).build();

        attacker.attack(defender);
        attacker.attack(defender);

        assertEquals(9, attacker.getHp());
    }

    @Test
    void polimorphismExample() {
        Creature defender = Creature.builder().aMaxHp(10).aAttack(Range.closed(2, 2)).aArmor(1).build();
        Creature attacker = Creature.builder().aMaxHp(10).aAttack(Range.closed(2, 2)).aArmor(1).build();
        ShootingCreature attackerShooter = new ShootingCreature(10, Range.closed(2, 2), 1);

        List<Creature> creatureList = new ArrayList<>();
        creatureList.add(attackerShooter);
        creatureList.add(attacker);

        creatureList.forEach(c -> c.attack(defender));
        assertEquals(8, defender.getHp());
        assertEquals(10, attackerShooter.getHp());
        assertEquals(9, attacker.getHp());
    }

    @Test
    void checkRandom() {
        Creature defender = Creature.builder().aMaxHp(100).aAttack(Range.closed(2, 2)).aArmor(0).build();
        Creature attacker = Creature.builder().aMaxHp(10).aAttack(Range.closed(1, 100)).aArmor(0).build();

        attacker.attack(defender);

        //!!!DON'T DO THAT!!!
        for (int i = 0; i < 1000000; i++) {
            assertTrue(defender.getHp() > 0 && defender.getHp() < 100);
        }
    }

    @Test
    void checkRandomWithMock() {
        Creature defender = Creature.builder().aMaxHp(100).aAttack(Range.closed(2, 2)).aArmor(0).build();
        Creature attacker = new Creature(10, Range.closed(1, 100), 0, new OurRandom());

        attacker.attack(defender);

        assertEquals(89, defender.getHp());
    }


    private class OurRandom extends Random{
        @Override
        public int nextInt(int bound) {
            return 10;
        }
    }
}
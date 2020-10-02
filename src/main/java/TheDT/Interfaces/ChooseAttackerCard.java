package TheDT.Interfaces;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface ChooseAttackerCard {
	void onChoseAttacker(AbstractCreature attacker, AbstractMonster m);

	default String hoverText(AbstractCreature hovered, AbstractMonster m) {
		return null;
	}
}

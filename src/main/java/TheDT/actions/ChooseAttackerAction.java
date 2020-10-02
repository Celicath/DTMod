package TheDT.actions;

import TheDT.Interfaces.ChooseAttackerCard;
import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.relics.MindBender;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Discovery;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class ChooseAttackerAction extends AbstractGameAction {
	public AbstractDTCard dtCard;
	public ChooseAttackerCard caCard;
	public AbstractMonster m;
	public ArrayList<AbstractCreature> attackers;
	public boolean isAttack;

	public static ChooseAttackerAction activeThis = null;
	public static final ArrayList<AbstractCard> dummyChoice = new ArrayList<AbstractCard>() {{
		add(new Discovery());
	}};

	public <T extends AbstractDTCard & ChooseAttackerCard> ChooseAttackerAction(T card, AbstractMonster m, boolean isAttack) {
		duration = Settings.ACTION_DUR_FAST;
		dtCard = card;
		caCard = card;

		this.isAttack = isAttack;
		this.m = m;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			activeThis = this;

			attackers = new ArrayList<>();
			attackers.add(AbstractDungeon.player);
			Dragon d = DragonTamer.getLivingDragon();
			if (d != null) {
				attackers.add(d);
			}

			AbstractRelic r = AbstractDungeon.player.getRelic(MindBender.ID);
			if (isAttack && r != null) {
				r.flash();
				for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
					if (!m.isDeadOrEscaped()) {
						attackers.add(m);
					}
				}
			}

			if (attackers.size() == 1) {
				choseAttacker(attackers.get(0));
				isDone = true;
				return;
			}

			AbstractDungeon.cardRewardScreen.chooseOneOpen(dummyChoice);
		}
		tickDuration();
	}

	public void choseAttacker(AbstractCreature attacker) {
		activeThis = null;
		caCard.onChoseAttacker(attacker, m);
	}
}

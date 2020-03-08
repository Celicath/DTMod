package TheDT.actions;

import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import TheDT.vfx.ForkedFlameEffect;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;

public class ForkedFlameAction extends AbstractGameAction {
	private boolean freeToPlayOnce;
	private int energyOnUse;
	private Dragon dragon;
	private AbstractDTCard card;

	private ArrayList<Integer> hpRemaining;
	private ArrayList<Integer> damages;
	private ArrayList<Integer> alive;

	public static final float PERIOD = 0.4f;

	public ForkedFlameAction(Dragon dragon, AbstractDTCard card, boolean freeToPlayOnce, int energyOnUse) {
		this.freeToPlayOnce = freeToPlayOnce;
		this.duration = Settings.ACTION_DUR_XFAST;
		this.actionType = ActionType.SPECIAL;
		this.energyOnUse = energyOnUse;
		this.card = card;
		this.dragon = dragon;

		ForkedFlameDamageAction.target_queue.clear();
		ForkedFlameDamageAction.target_damage_queue.clear();
	}

	public void update() {
		isDone = true;
		if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
			return;
		}

		card.target_y = Settings.HEIGHT / 2.0f + 300.0f * Settings.scale;

		hpRemaining = new ArrayList<>();
		damages = new ArrayList<>();
		alive = new ArrayList<>();
		int index = 0;
		for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (m.isDeadOrEscaped()) {
				hpRemaining.add(0);
				damages.add(0);
			} else {
				hpRemaining.add(m.currentBlock + m.currentHealth + TempHPField.tempHp.get(m));
				card.calculateCardDamage(m);
				damages.add(card.dtDragonDamage);
				alive.add(index);
			}
			index++;
		}

		int effect = EnergyPanel.totalCount;
		if (energyOnUse != -1) {
			effect = energyOnUse;
		}

		if (AbstractDungeon.player.hasRelic(ChemicalX.ID)) {
			effect += 2;
			AbstractDungeon.player.getRelic(ChemicalX.ID).flash();
		}

		if (effect <= 0) {
			return;
		}
		if (!freeToPlayOnce) {
			AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
		}

		for (int i = 0; i < effect; i++) {
			ArrayList<AbstractMonster> targets = new ArrayList<>();
			ArrayList<Integer> targets_damage = new ArrayList<>();
			if (alive.size() == 1) {
				targets.add(AbstractDungeon.getCurrRoom().monsters.monsters.get(alive.get(0)));
				targets_damage.add(damages.get(alive.get(0)));
			} else if (alive.size() > 1) {
				int index1 = AbstractDungeon.cardRandomRng.random(alive.size() - 1);
				int index2 = AbstractDungeon.cardRandomRng.random(alive.size() - 2);
				if (index2 >= index1) {
					index2++;
				}
				index1 = alive.get(index1);
				index2 = alive.get(index2);

				targets.add(AbstractDungeon.getCurrRoom().monsters.monsters.get(index1));
				targets.add(AbstractDungeon.getCurrRoom().monsters.monsters.get(index2));
				targets_damage.add(damages.get(index1));
				targets_damage.add(damages.get(index2));

				hpRemaining.set(index1, hpRemaining.get(index1) - damages.get(index1));
				if (hpRemaining.get(index1) <= 0) {
					alive.remove(new Integer(index1));
				}
				hpRemaining.set(index2, hpRemaining.get(index2) - damages.get(index2));
				if (hpRemaining.get(index2) <= 0) {
					alive.remove(new Integer(index2));
				}
			}
			if (targets.size() > 0) {
				AbstractDungeon.effectList.add(new ForkedFlameEffect(dragon, PERIOD * i, targets, targets_damage));
				addToTop(new ForkedFlameDamageAction(dragon));
			}
		}
	}
}

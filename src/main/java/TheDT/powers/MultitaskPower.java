package TheDT.powers;

import TheDT.DTModMain;
import TheDT.Interfaces.CreateBurnPower;
import TheDT.Interfaces.OnApplyWeakPower;
import TheDT.Interfaces.RecoloredPower;
import TheDT.Interfaces.SwitchPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static TheDT.utils.StringHelper.highlightedText;

public class MultitaskPower extends AbstractPower implements CreateBurnPower, SwitchPower, OnApplyWeakPower, RecoloredPower {
	public static final String RAW_ID = "MultitaskPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public int taskCompleted = -1;
	public boolean disabled = false;

	public MultitaskPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.loadRegion("wave_of_the_hand");
	}

	@Override
	public Color getIconColor() {
		return new Color(1.0f, 0.5f, 0.4f, 1.0f);
	}

	@Override
	public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
		super.renderIcons(sb, x, y, new Color(1.0f, 0.5f, 0.4f, c.a));
	}

	public void taskClear() {
		disabled = true;
		flash();
		updateDescription();
		addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				disabled = false;
				isDone = true;
			}
		});
	}

	public void setTask(int task) {
		if (disabled) {
			return;
		}
		if (taskCompleted == -1) {
			taskCompleted = task;
			taskClear();
		} else if (taskCompleted != task) {
			taskCompleted = -1;
			taskClear();
			addToBot(new DrawCardAction(amount));
		}
	}

	@Override
	public void onBurnCreated() {
		setTask(0);
	}

	@Override
	public void onApplyWeak(int amount) {
		setTask(1);
	}

	@Override
	public void onSwitch() {
		setTask(2);
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
		for (int i = 2; i < DESCRIPTIONS.length; i++) {
			description += " NL " + (i == 2 + taskCompleted ? highlightedText(DESCRIPTIONS[i]) : DESCRIPTIONS[i]);
		}
	}
}

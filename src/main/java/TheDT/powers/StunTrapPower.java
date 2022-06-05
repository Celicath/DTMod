package TheDT.powers;

import TheDT.DTModMain;
import TheDT.Interfaces.RecoloredPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

public class StunTrapPower extends AbstractDelayedPower implements RecoloredPower {
	public static final String RAW_ID = "StunTrapPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	AbstractMonster target;
	int draw;

	public StunTrapPower(AbstractMonster target, AbstractCreature owner, int turn, int draw) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.target = target;
		this.amount = turn;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.loadRegion("channel");
		this.draw = draw;
	}

	@Override
	public Color getIconColor() {
		return new Color(1.0f, 1.0f, 0.35f, 1.0f);
	}

	@Override
	public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
		super.renderIcons(sb, x, y, new Color(1.0f, 1.0f, 0.35f, c.a));
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[0] + target.name + DESCRIPTIONS[3] + draw + DESCRIPTIONS[4];
		} else {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + target.name + DESCRIPTIONS[3] + draw + DESCRIPTIONS[4];
		}
	}

	@Override
	public void onActivate() {
		addToBot(new VFXAction(new WeightyImpactEffect(target.hb.cX, target.hb.cY)));
		addToBot(new WaitAction(0.4F));
		addToBot(new ApplyPowerAction(target, owner, new StunMonsterPower(target, 1), 1));
		addToBot(new DrawCardAction(draw));
	}
}

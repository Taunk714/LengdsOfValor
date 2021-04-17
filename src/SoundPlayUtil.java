public class SoundPlayUtil {

    public static void playError(){
        new PlaySound("InputError").start();
    }

    public static void playEnterCell(){
        new PlaySound("Scanner").start();
    }

    public static void playGameStart(){
        new PlaySound("GameStart").start();
    }

    public static void playJoinTeam(){
        new PlaySound("JoinTeam").start();
    }

    public static void playHeroAttackNormal(){
        new PlaySound("HeroAttack" + randomUtil.nextInt(2)).start();
    }

    public static void playHeroAttackSword(){
        new PlaySound("HeroAttackSword").start();
    }

    public static void playMonsterAttack(){
        new PlaySound("MonsterAttack").start();
    }

    public static void playMonsterHurt(){
        new PlaySound("MonsterHurt" + randomUtil.nextInt(2)).start();
    }

    public static void playHeroHurt(){
        new PlaySound("HeroHurt" + randomUtil.nextInt(2)).start();
    }

    public static void playWin(){
        new PlaySound("Win").start();
    }

    public static void playLose(){
        new PlaySound("Lose").start();
    }

    public static void playArmor(){
        new PlaySound("ArmorChange").start();
    }

    public static void playIceSpell(){
        new PlaySound("IceSpell").start();
    }

    public static void playFireSpell(){
        new PlaySound("FireSpell").start();
    }

    public static void playLightningSpell(){
        new PlaySound("LightningSpell").start();
    }

    public static void playLevelUp(){
        new PlaySound("LevelUp").start();
    }

    public static void playGoIntoMap(){
        new PlaySound("GoIntoMap").start();
    }

    public static void playMonsterDead(){
        new PlaySound("MonsterDead").start();
    }

    public static void playSpiritHurt(){
        new PlaySound("SpiritsHurt").start();
    }

    public static void playAttackMiss(){
        new PlaySound("AttackMiss").start();
    }

    public static void playBuyOrSell(){
        new PlaySound("BuyOrSell").start();
    }

    public static void playTeleport(){
        new PlaySound("Teleport").start();
    }
}

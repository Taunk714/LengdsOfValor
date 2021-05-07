>
> Team Member: Yuqiu Lin[(@Taunk714)](https://github.com/Taunk714) &
> Tian Yu[(@Tianjzjz)](https://github.com/tianjzjz)


## 1. introduction
- We followed the basic logistics described in the assignment document
- **We have color & sound!!!**
- Our project is mainly based on Yuqiu Lin's code, because her printing message 
  is more comfortable. And the printing method is heavily dependent on her 
  xxCreator class. Tian Yu's code is well-organized. Her Structure is really 
  comfortable and complete. And her xxUtil class really helps Yuqiu remove my duplicate codes. 
  And also we change implementation of item-using function based on Tian Yu's thought.

ps. we extended Legends and implemented Legends of Valor in the same project. But we remove the Legends(4 class) part 
before uploading because the requirement only need LOV and then you don't need to read codes that are for Legends.

### 1.1.Before running
1. Change ``configUtil.java`` Line 18. The most convenient is to use the absolute path of ``config.properties``
2. Change `config.properties`. The first two attributes should be the path of data folder`Legends_Monsters_and_Heroes` and music folder `Music`. Also, the absolute path is the most convenient.

## 3. structure

* **StartGame**: main class.<br><br>


* ***Game**: Abstract Game interface.*
* ***RpgGame**: Abstract Game interface.*
* **LOVGame implements RpgGame**: LOV It's a singleton. I make it a singleton because it's really helpful to implement
  show map and information at any moment. And since it's not a cyber game, it will only have one Legend class.<br><br>


* ***Grid**: Grid interface.*
* **LOVGrid implements Grid**: LOV Grid class. The playing grid. Consist of different cells.<br><br>

* **MovingUnit**: Interface. A single moving unit. In Legends, one Team is a MovingUnit. In LOV, a Character is a MovingUnit<br><br>


* ***Cell**: Cell interface. form the grid.*
* ***AccessibleCell**: AccessibleCell interface. Cells except Inaccessible Cell should implment this interface.*
* **InaccessibleCell implements Cell**: Inaccessible cell. The team can't go here, will reject and go back to the previous cell.
* **PlainCell<T extends Character> implements Cell, AccessibleCell<T>**: Common cell. Can change weapon or armor freely if the team doesn't meet monsters.
* **BushCell extends PlainCell**: BushCell, one of the three special cell. Increase the dexterity.
* **CaveCell extends PlainCell**: CaveCell, one of the three special cell. Increase the agility.
* **KoulouCell extends PlainCell**: KoulouCell, one of the three special cell. Increase the strength.
* **NexusCell<T extends Character> extends PlainCell<T>**: NexusCell, where heroes and monsters are born.
* **HeroNexusCell extends NexusCell<Hero>**: HeroNexusCell. Heroes' Nexus Cell, which is also a market, can buy or sell items.<br><br>

* ***Buy**: Buy Interface. Those can be bought in th market.*
* ***Sell**: Sell Interface. Those can be sold in the market.*
* **Item implements Buy, Sell, Tablefiable**: Abstract Item class.
* **Armor extends Item**: Armor class. Can be worn.
* **Weapon extends Item**: Weapon class. Can be hold in the hand.
* **Potion extends Item**: Weapon class. Can only use once, but the skill bonus will last forever.<br><br>
* **Spell extends Item**: Spell class. Can only use once, but the effect will last forever.
* **FireSpell extends Spell**: FireSpell class. Decrease the target's damage.
* **IceSpell extends Spell**: IceSpell class. Decrease the target's defence.
* **LightningSpell extends Spell**: LightningSpell class. Decrease the target's dodge chance.<br><br>

* ***Fight**: Fight Interface. Those who can fight.*
* ***Character implements Tablefiable, MovingUnit**: Abstract character class.*
  
* **Hero extends Character implements Fight**: Hero class. All the heroes are created by HeroCreator
* **Paladin extends Hero**: Paladin class, one of the three types of hero. All the heroes are created by HeroCreator
* **Sorcerer extends Hero**: Sorcerer class, one of the three types of hero. All the heroes are created by HeroCreator
* **Warrior extends Hero**: Paladin class, one of the three types of hero. All the heroes are created by HeroCreator

* **Monster extends Character implements Fight**: Monster class. All the monsters are created by MonsterCreator.
* **Spirit extends Monster**: Spirit class. One of the three types of monster.
* **Exoskeleton extends Monster**: Monster class. Exoskeletion class. One of the three types of monster.
* **Dragon extends Monster**: Monster class. Dragon class. One of the three types of monster.
* **Team<T extends Character> implements Iterable<T>**: Team class. Can be used for both Hero and Monster.
* **LOVHeroTeam implements Team<Hero>**: LOVHeroTeam class. The Hero team in LOV.
* **LOVHeroTeam implements Team<Hero>**: LOVMonster class. The Monster Team in LOV.<br><br>

* **ItemCreator**: ItemCreator class. It's a singleton. Read data from the file. When hero decide the item, use this instance to create corresponding item.
* **HeroCreator**: HeroCreator class. It's a singleton. Read data from the file. Every hero is unique, can only be instantiated once.
  One game only have one HeroCreator, once the hero was chosen, he can't be chosen anymore.
* **MonsterCreator**: MonsterCreator class. Read data from the file. Create the monster by level.<br><br>
  


* **Skill**: Skill class. Store the favor information.
* **Bag**: Bag class. Hero stores items in the Bag.<br><br>

* **Tablefiable**: An interface. Sorry the name might be weird. Those who can be shown in the table need to implement this interface. 
  There is an getAttr() method, get all the attributes of one instance and store in a `String[]`.<br><br>

* **PlaySound extends Thread**: An class tha play sound. <br><br>

* **MyFont**: static class. Store some style information like bold, color or background color.<br><br>* **configUtil**: used to read in the configurations of the game settings
* **SoundPlayUtil**: call the static methods in this class to play different sounds. We can change the sound for a specific function easily in this class instead of going to different class
* **printUtil**: used to simplify the output printing. 
* **randomUtil**: used to generate random number
* **scannerUtil**: used to read in the user's input<br><br>

## how to run

- javac *.java
- java StartGame.java

## demo
todo...



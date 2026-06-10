# Hard Pity

**Hard Pity** is a survival\-themed meme mod optimized for Minecraft\. It adds a pity drop system for **Wither Skeleton Skulls, Tridents, Dragon Eggs, and Netherite Scraps**, effectively solving the frustrating "bad luck" issue players face when farming these rare items\.

---

## Gacha Pool System

### Trigger Mechanism

Players will receive **1 basic gacha draw** for the corresponding pool each time they defeat the following mobs:

- Wither Skeleton → Wither Skeleton Pool

- Drowned → Drowned Pool

- Piglin / Piglin Brute → Piglin Pool

- Ender Dragon → Ender Dragon Pool

**Extra Draw Rules**:

- Tools or weapons with the **Looting enchantment** grant additional bonus draws based on the enchantment level

- The draw count for each gacha pool is **calculated independently**

### Gacha Pool Rewards

|Pool|4\-Star Reward|5\-Star Reward \(Success / Hard Pity\)|5\-Star Reward \(Misroll\)|
|---|---|---|---|
|Wither Skeleton|Wither Rose|**Wither Skeleton Skull**|Skeleton Skull|
|Drowned|Copper Ingot|**Trident**|Diamond Sword|
|Piglin / Piglin Brute|Gold Ingot|**Netherite Scrap**|Brown Dye|
|Ender Dragon|Dragon Head|**Dragon Egg**|Egg|

---

## Probability \& Pity Rules

### 5\-Star \(Rare Item\) Pity Mechanism

Players can customize the maximum pity count in the config file\. The default pity count is 40 draws; players are guaranteed to obtain a 5\-star rare item once the cumulative draw count of a single pool reaches the set pity threshold\. The mod provides three customizable probability models \(default: Mihoyo Model\) with distinct rolling rules as follows:

#### Mihoyo Model \(Default\)

This model adopts a probability curve similar to classic Mihoyo gacha mechanics\. The last 4 draws before hard pity feature sharply increased rates: 16%, 33%, 51%, and 80%\. All draws before the final 5th pity draw have a fixed single pull probability calculated by $0.21 \div (Pity Count - 5)$, delivering stable low probability in early rolls and sharp rate surges near maximum pity\.

#### Linear Model

This model features a uniformly increasing probability\. The single pull probability follows the formula: $(Current Draw Count + 1) \div Total Pity Count$\. The probability rises linearly from 0% to 100% as draws accumulate, offering a steady and predictable growth trend\.

#### Exponential Model

This model delivers a smoother probability growth curve with no abrupt rate changes\. The single pull probability formula is: $0.3679^{(Pity Count - Current Draw Count)} - 1$\. Compared with the other two models, it provides gentle and gradual probability improvement throughout the entire pity cycle\.

### Soft Pity \& Misroll Mechanism

- The default soft pity **success rate \(no misroll\)** is 50%, and this value can be freely customized in the config file to adjust the success/misroll ratio\.

- If a 5\-star roll results in a misroll, **the next 5\-star roll is guaranteed to be the target rare item** \(hard pity\), and the pity counter will reset afterward\.

### 4\-Star Reward Pity Mechanism

- The independent 4\-star pity system defaults to 20 draws, which can be customized in the config file\. The 4\-star pity counter operates independently and does not interfere with the 5\-star pity counter\.

- 4\-star rolls also benefit from the Looting enchantment and can receive additional bonus draws\.

---

## Light Capture Mechanism

This mechanism is stored as NBT data in player files and shares a universal counter across all gacha pools\. The counter starts at 0 and ranges from \-1, 0, 1, to 2\.

The counter increases by 1 when a player gets a misroll on soft pity and decreases by 1 when a player succeeds on soft pity \(minimum value locked at \-1\)\. When the counter is 1, players gain a 6% chance to trigger the "Light Capture" effect on soft pity rolls — a successful trigger guarantees a valid target item and resets the counter to 0, while a failed trigger follows normal misroll rules\. When the counter is 2, all soft pity rolls are guaranteed to succeed with no misrolls, and the counter resets to 0\. On the premise of the default 50% misroll rate, this mechanism raises the overall long\-term soft pity success rate to approximately 54\.94%\.

---

## FAQ

Q: Can I include this mod in my custom modpack?

A: Yes, you are free to add this mod to any modpacks\.

Q: Can I modify or port this mod to other platforms?

A: Yes, you can fork the project on GitHub at any time for personal modification and cross\-platform porting\.


# TNTRun - Kotlin
```text
ねこぞうねこ および Team Nekozouneko での配信用ゲーム "TNTRun" をKotlin言語で改装したもの。
```

## System

---

基本的には https://github.com/TeamNekozouneko/TNTRun と同じような仕様となっています。
それをプログラミング言語「Kotlin」で改装し、安定化、軽量化させたものになります。

## Stack

---

- ProtocolLib (Protocol library for Bukkit)
- GSon (Json library for java/kt)
- WorldGuard / WorldEdit (region system for TNTRun)
- FastBoard (Easy to use scoreboard library for Bukkit)

## for Dev (Windows only)

ビルド、テスト用に、cmdファイルを作成しています。
```shell
./build
```
でビルド、テスト用サーバーのpluginsフォルダへのコピーが自動でされるようになっています。
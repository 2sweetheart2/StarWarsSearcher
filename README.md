# StarWarsSearcher

поиск персонажей или звёздных кораблей из вселенной Star Wars

---

в коде есть java Doc для функций или классов, но:

- [Mainactivity](https://github.com/2sweetheart2/StarWarsSearcher/blob/master/app/src/main/java/me/sweetie/starwarssearcher/MainActivity.kt) - основной класс, который запускается
-   фрагменты
-   - [SearchFragment](https://github.com/2sweetheart2/StarWarsSearcher/blob/master/app/src/main/java/me/sweetie/starwarssearcher/SearchFragment.kt) - класс, в котором происходит поиск персонажей или звездолётов
    - [FavoriteFragment](https://github.com/2sweetheart2/StarWarsSearcher/blob/master/app/src/main/java/me/sweetie/starwarssearcher/FavoriteFragment.kt) - класс, в котором показываются люди изи звездолёты, которые добавили в избранное
- [Requests](https://github.com/2sweetheart2/StarWarsSearcher/tree/master/app/src/main/java/me/sweetie/starwarssearcher/requests)
- - [SwAPI](https://github.com/2sweetheart2/StarWarsSearcher/blob/master/app/src/main/java/me/sweetie/starwarssearcher/requests/SwAPI.kt) - класс, к которому все остальные обращаются к api
  - [HTTPRequests](https://github.com/2sweetheart2/StarWarsSearcher/blob/master/app/src/main/java/me/sweetie/starwarssearcher/requests/HTTPSRequests.kt) - класс, к которому обращается только SwAPI, для отправки запроса и получения ответа
- [Объекты для парсинга с API](https://github.com/2sweetheart2/StarWarsSearcher/tree/master/app/src/main/java/me/sweetie/starwarssearcher/objects)
- [CallBack'И, для API](https://github.com/2sweetheart2/StarWarsSearcher/tree/master/app/src/main/java/me/sweetie/starwarssearcher/interfaces)
---
с ближеный [apk](https://github.com/2sweetheart2/StarWarsSearcher/releases/tag/last) на всякий

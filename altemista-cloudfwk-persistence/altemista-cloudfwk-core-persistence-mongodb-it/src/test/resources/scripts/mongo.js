// db = connect("localhost:27020");
// use test;
db.dropDatabase();

db.kitchen.insert({
    "_id" : NumberLong(1),
    "_class" : "org.altemista.cloudfwk.it.persistence.model.Recipe",
    "name" : "chocolate cake",
    "bakingTemperature" : 180,
    "ingredients": [
        {
            "_id" : NumberLong(1),
            "_class" : "org.altemista.cloudfwk.it.persistence.model.Ingredient",
            "name" : "chocolate",
        },
        {
            "_id" : NumberLong(1),
            "_class" : "org.altemista.cloudfwk.it.persistence.model.Ingredient",
            "name" : "sugar",
        }
    ],
    "bakingTime" : 20
});
db.kitchen.insert({
    "_id" : NumberLong(2),
    "_class" : "org.altemista.cloudfwk.it.persistence.model.Recipe",
    "name" : "chocolate cookies",
    "bakingTemperature" : 220,
    "ingredients": [
        {
            "_id" : NumberLong(1),
            "_class" : "org.altemista.cloudfwk.it.persistence.model.Ingredient",
            "name" : "chocolate",
        },
        {
            "_id" : NumberLong(3),
            "_class" : "org.altemista.cloudfwk.it.persistence.model.Ingredient",
            "name" : "flour",
        }
    ],
    "bakingTime" : 10
});
db.kitchen.insert({
    "_id" : NumberLong(3),
    "_class" : "org.altemista.cloudfwk.it.persistence.model.Recipe",
    "name" : "fish and chips",
    "bakingTemperature" : 180,
    "ingredients": [
        {
            "_id" : NumberLong(4),
            "_class" : "org.altemista.cloudfwk.it.persistence.model.Ingredient",
            "name" : "fish",
        },
        {
            "_id" : NumberLong(5),
            "_class" : "org.altemista.cloudfwk.it.persistence.model.Ingredient",
            "name" : "chimps",
        }
    ],
    "bakingTime" : 5
});

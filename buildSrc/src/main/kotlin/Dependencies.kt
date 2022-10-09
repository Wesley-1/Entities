// Contains all version information for out dependencies.
object Versions {
    const val SPIGOT_VERSION = "1.17.1-R0.1-SNAPSHOT"
    const val LOMBOK_VERSION = "1.18.20"
    const val REFLECTIONS_VERSION = "0.10.2";
    const val WORLDGUARDWRAPPER = "1.2.0-SNAPSHOT"
    const val LUCKO_HELPER_VERSION = "5.6.10"
    const val LUCKO_SQL_VERSION = "1.3.0"
    const val LUCKO_MONGO_VERSION = "1.2.0"
    const val  LUCKO_REDIS_VERSION = "1.2.0"
    const val LUCKO_LILLY_PAD_VERSION = "2.2.0"
    const val LUCKO_PROFILES_VERSION = "1.2.0"
    const val ROCKS_DB_VERSION = "6.6.4"
    const val GUICE_VERSION = "5.1.0"
}

object Dependencies {
    const val _SPIGOT = "org.spigotmc:spigot:${Versions.SPIGOT_VERSION}"
    const val SPIGOT = "org.spigotmc:spigot-api:${Versions.SPIGOT_VERSION}"
    const val LOMBOK = "org.projectlombok:lombok:${Versions.LOMBOK_VERSION}"
    const val REFLECTIONS = "org.reflections:reflections:${Versions.REFLECTIONS_VERSION}"
    const val WORLDGUARDWRAPPER = "org.codemc.worldguardwrapper:worldguardwrapper:${Versions.WORLDGUARDWRAPPER}"
    const val LUCKO_HELPER = "me.lucko:helper:${Versions.LUCKO_HELPER_VERSION}"
    const val LUCKO_SQL = "me.lucko:helper-sql:${Versions.LUCKO_SQL_VERSION}"
    const val LUCKO_REDIS = "me.lucko:helper-redis:${Versions.LUCKO_REDIS_VERSION}"
    const val LUCKO_MONGO = "me.lucko:helper-mongo:${Versions.LUCKO_MONGO_VERSION}"
    const val LUCKO_LILLYPAD = "me.lucko:helper-lilypad:${Versions.LUCKO_LILLY_PAD_VERSION}"
    const val LUCKO_PROFILES = "me.lucko:helper-profiles:${Versions.LUCKO_PROFILES_VERSION}"
    const val ROCKS_DB = "org.rocksdb:rocksdbjni:${Versions.ROCKS_DB_VERSION}"
    const val GUICE = "com.google.inject:guice:${Versions.GUICE_VERSION}"
}
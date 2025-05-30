# Shelduer

> **Declarative Access Control for Self-Modifying and Dynamic Code**

![Build](https://img.shields.io/badge/build-passing-brightgreen) ![License](https://img.shields.io/badge/license-MIT-blue)

---

## 🚀 Overview

**Shelduer** is a framework that replaces `volatile`, `synchronized`, and `Atomic` structures with a **declarative, pluggable, and aspect-oriented** access scheduler.

It is designed for:
- Self-modifying AI systems
- Runtime-generated code
- Hot-deployable services
- Dynamic or interpreted environments (e.g., plugin-based systems)

---

## 🔥 Why Not Volatile or Synchronized?

| Feature / Mechanism     | `volatile` | `synchronized` | `Atomic` | `Shelduer`            |
|------------------------|------------|----------------|----------|------------------------|
| Works across class reloads | ❌         | ❌             | ❌       | ✅                     |
| Declarative            | ❌         | ❌             | ❌       | ✅ (with annotations)   |
| Hot-swappable logic    | ❌         | ❌             | ❌       | ✅                     |
| Key-based locking      | ❌         | 🔸 Object only  | ❌       | ✅ (String or custom)  |
| Introspectable policy  | ❌         | ❌             | ❌       | ✅                     |

---

## ✨ Key Concepts

### @AccessSchedule
```java
@AccessSchedule(
    key = "#{#user.id}",
    policy = AccessPolicy.EXCLUSIVE,
    timeoutMs = 5000
)
public void updateProfile(User user) {
    // Controlled access to "user.id" resource
}
```

### KeyProvider Interface
```java
public interface KeyProvider {
    String resolveKey(JoinPoint joinPoint, Object[] args);
}
```

Use it to resolve dynamic access keys at runtime.

### LedgerTable
An in-memory or distributed table tracking which logical keys are being used and by whom. Think of it as a programmable lock manager.

### Access Policies
```java
public enum AccessPolicy {
    EXCLUSIVE,
    FAIR,
    LAST_WRITE_WINS,
    SKIP_IF_BUSY
}
```

---

## 🧠 Designed for AI & Self-Mutating Systems
Shelduer is perfect for:
- AI models rewriting their own methods
- Runtime mutation of services
- Systems using interpreted bytecode or scripts

```java
@AccessSchedule(key = "compilation")
public void mutateOwnCode() {
    // Safe zone for AI to recompile or alter logic
}
```

---

## 🛠️ Roadmap
- [x] Basic annotation processor with Spring AOP
- [x] In-memory `LedgerTable`
- [ ] YAML-based access policy config
- [ ] Distributed ledger support (Redis / embedded)
- [ ] Runtime rule injection API
- [ ] Dynamic key resolution via SpEL and providers
- [ ] Support for timeout and queue-based access control
- [ ] Test suite with high concurrency examples
- [ ] Integration with dynamic bytecode engines

---

## 🤘 Philosophy

> "Don’t lock threads. Schedule intent."

Let your code evolve. Let your AI adapt. Let Shelduer control the flow.

---

## License
MIT License

---

## Author
[@swampus](https://github.com/swampus) — Creator of sentient scheduling primitives 😈



🔹 1. ✅ Базовый минимум (почти готов)
Компонент	Статус	Комментарий
Чистая архитектура	✅	Отличная модульность и SPI
Расширяемость	✅	Аннотация, интерфейсы, SpEL
Простая интеграция	✅	Spring Boot Starter уже есть
Примеры	⚠️	Нужно 2–3 сценария: REST-контроллер, очередь, Redis
Документация	⚠️	README.md норм, но нужен upgrade с диаграммами
Юнит-тесты	⚠️	Core-тесты есть? Добавь ещё edge-cases

🔸 2. 💪 Инфраструктурный уровень
📌 shelduer-redis
RedisAccessLedger

TTL + fairness + журналирование

Интеграция через Spring Boot @ConditionalOnProperty("shelduer.ledger.type=redis")

📌 shelduer-metrics
Подключение Micrometer

Экспозиция:

кол-во конфликтов

среднее время ожидания

top-5 самых частых ключей

Prometheus endpoint: /actuator/metrics/shelduer.*

📌 Расширяемый AccessContext
Объект-контейнер вместо сырых аргументов

Поддержка приоритета, причины, пользователя

🔸 3. 🧠 Умный контроль доступа
📌 ShelduerPolicyEngine
Сценарии вида: "user.admin" всегда получает доступ мгновенно

Поддержка SpEL-like правил + whitelist/blacklist

Подключается как бин AccessPolicyResolver

📌 Плагинная система
AccessDecisionVoter (как в Spring Security)

Внешние правила из базы

🔸 4. 💼 Продакшн-готовность и DevOps
📦 CI/CD:
GitHub Actions или Jenkins

Сборка → Тесты → Publish snapshot в GitHub Packages

(позже) Release в Maven Central

🧪 100% покрытие core API
Блокировки, таймауты, исключения, стресс-тесты

🔍 Логирование:
События блокировки

Thread name, ключ, политика, таймаут, успех/ошибка

shelduer.access.eventLogger=console/json

🔸 5. 🏗️ Интеграция с другими системами
Spring Web: блокировать REST методы

Kafka listener: ограничить одновременную обработку

Hibernate: предотвратить дубли при @Transactional

Annotations-on-Fields для @AccessControlled private Resource resource;

🔸 6. 🎓 Демонстрации и документация
docs/Shelduer-Architecture.md — как всё устроено

docs/UseCases.md — 5+ сценариев

img/ — диаграмма зависимостей и потоков

README.md с бейджами: CI, Test Coverage, Maven Central

🔸 7. 🌐 Обёртка и сайт
Сайт на GitHub Pages

Ссылка на Javadoc

Кнопка "Try it" с Docker-контейнером

Визуализатор текущих лоченных ключей (в будущем)

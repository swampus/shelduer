/ | | | | |
| ( | |_ | | | ___ _ __ ___ _ __
_ | ' \ / _ \ | |/ _ \ '_ \ / _ \ '|
) | | | | __/ | | / | | | / |
|/|| |_|_|||_|| ||___|_|


# Shelduer

🛡️ Declarative runtime access control system for mutable and AI-generated Java code.

> What if `volatile` is no longer enough?

Shelduer provides annotation-based access governance that survives class reloading, self-modifying code, and AI-driven architecture evolution.

---

## ✨ Features

- `@AccessControlled` annotation for fields and methods
- Runtime ledger with allow/block logic
- External `ShelduerClient` for integration with AI systems
- Works without `volatile`, `synchronized`, or shared memory primitives


## 🔐 Access Policies

Shelduer supports several policies for controlling concurrent access to resources:

| Policy          | Description |
|-----------------|-------------|
| `EXCLUSIVE`     | Only one thread can enter the critical section at a time. Others wait. |
| `SKIP_IF_BUSY`  | If the resource is already taken, the call is skipped immediately. |
| `FAIR`          | Threads acquire access in FIFO order. Uses `ReentrantLock(true)` under the hood. |

### 🎯 Example: FAIR policy

```java
@AccessControlled(key = "#userId", policy = AccessPolicy.FAIR)
public void accessData(String userId) {
    // Guaranteed first-come, first-served access.
}
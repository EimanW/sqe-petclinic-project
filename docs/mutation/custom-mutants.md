# Custom mutation operators (2026-11-27)

| ID | Operator | Mutation | Result |
|---|---|---|---|
| CM1-a | Age-limit shift | Upper age limit 50 -> 49 years | KILLED |
| CM1-b | Age-limit shift | Upper age limit 50 -> 51 years | KILLED |
| CM1-c | Age-limit shift | Future-date limit shifted by one day | KILLED |
| CM2-a | Guard removal | Role check removed from every owner endpoint | KILLED |
| CM2-b | Guard removal | Ownership check replaced by global pet lookup | KILLED |

Killed 5 of 5

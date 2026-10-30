# Навчальна платформа «ЕдуПортал» (LMS)

**Дисципліна:** Технології забезпечення якості програмних засобів (ТЗЯПЗ)  
**Продукт:** «ЕдуПортал» (система дистанційного навчання, LMS, Продукт №6)  
**Команда:** EduGuard (Команда F, група ФБ-44)  

### Склад команди та розподіл ролей за спринтами (5 осіб)

| Учасник команди | Псевдонім | Роль у Спринті 1 | Роль у Спринті 2 | Роль у Спринті 3 |
|:---|:---|:---|:---|:---|
| **Олександр Панайотов** | `5ani0chek` (Sasha26062007) | Інженер з тестування (ІТ) | **Керівник тестування (Test Lead)** | **Скрам-майстер / Рецензент (Scrum Master)** |
| **Денис Бучинський** | `Denchik` | Керівник тестування + Скрам-майстер (КТ+СМ) | **Тест-аналітик (Test Analyst)** | **Керівник тестування (Test Lead)** |
| **Володимир Костенко** | `Vov4ik` | Тест-аналітик (ТА) | **Інженер з тестування (Test Engineer)** | **Тест-аналітик (Test Analyst)** |
| **Олександр Пархоменко** | `ParkhomenkoO` | *(приєднався зі Спринту 2)* | **Аудитор-опонент (Quality Auditor / Opponent)** | **Інженер з тестування (Test Engineer)** |
| **Олександр Кираківський** | `KyrakivskyiO` | *(приєднався зі Спринту 2)* | **Скрам-майстер / Рецензент (Scrum Master / Reviewer)** | **Аудитор-опонент (Quality Auditor)** |

---

## 📑 Загальнокомандна документація
- **[Статут команди (docs/team/charter.md)](docs/team/charter.md)** — регламенти Scrum, правило «двох аудиторів», Definition of Done та матриця ротації.

---

## 🚀 Спринт 3: «Тест-кейси та дефекти» (Поточний реліз)

**Досьє якості v3:** 30 тест-кейсів, 10 звітів про дефекти (GitHub Issues `#1`–`#10`), повний журнал прогону, сліпий аудит і підписаний DoD.

### 📌 Основні артефакти Спринту 3:
1. **[Досьє якості v3 (docs/sprint3/README.md)](docs/sprint3/README.md)** — зведені метрики, покриття SRS 1.1, обмеження та DoD.
2. **[Комплект тест-кейсів (docs/sprint3/test-cases.xlsx)](docs/sprint3/test-cases.xlsx)** — 30 кейсів, аркуші `Test cases`, `Coverage`, `Review`.
3. **[Журнал прогону (docs/sprint3/execution-log.xlsx)](docs/sprint3/execution-log.xlsx)** — `Run 1` (30 рядків) та `Environment`.
4. **[Звіти про дефекти (docs/sprint3/bug-reports.md)](docs/sprint3/bug-reports.md)** — `BUG-001..BUG-010` із вкладеннями в [attachments](docs/sprint3/attachments).
5. **[Акт сліпого аудиту (docs/sprint3/audit.md)](docs/sprint3/audit.md)** — відтворення `BUG-005`, `BUG-002`, `BUG-004`.
6. **[Дошка задач Спринту 3 (docs/sprint3/task-board.md)](docs/sprint3/task-board.md)** — 25 задач, 5 ролей.
7. **[Протокол ретроспективи (docs/sprint3/retro-sprint3.md)](docs/sprint3/retro-sprint3.md)** — Start / Stop / Continue.
8. **[Сценарій Демо (docs/sprint3/demo-script-sprint3.md)](docs/sprint3/demo-script-sprint3.md)** — 10-хвилинний захист.
9. **[Презентація до захисту Спринту 3](docs/sprint3/EDUPORTAL_Sprint3_Presentation.pptx)**.

---

## 📦 Спринт 2: «Аудит вимог і стратегія тестування» (Архів інкременту)

### 📊 Презентація до захисту Спринту 2:
- **[Презентація PowerPoint (10 хв)](docs/sprint2/EDUPORTAL_Sprint2_Presentation.pptx)** — повний звіт за Спринтом 2 з нотатками доповідачів до кожного слайда.

### 📌 Основні інженерні артефакти Спринту 2:
1. **[Реєстр дефектів вимог (docs/sprint2/requirements-defects.xlsx)](docs/sprint2/requirements-defects.xlsx)** — 24 виявлені дефекти (по 4 на кожен із 6 критеріїв якості), розподіл розділів, аркуш «Спірні» та індивідуальні аркуші 5 аудиторів.
2. **[Виправлена специфікація вимог (docs/sprint2/SRS-1.1.md)](docs/sprint2/SRS-1.1.md)** — оновлена версія вимог із детальним Журналом змін (Change Log) на початку.
3. **[Вихідна специфікація вимог (docs/sprint2/SRS-1.0.md)](docs/sprint2/SRS-1.0.md)** — первинний документ «для аудиту».
4. **[План тестування продукту (docs/sprint2/test-plan.md)](docs/sprint2/test-plan.md)** — комплексна стратегія: 7 рівнів піраміди тестування, 16 застосовних типів тестування з Цілями та Описами, обґрунтування 2 незастосовних типів.
5. **[Таблиця задач спринту (docs/sprint2/tasks.md)](docs/sprint2/tasks.md)** — дошка виконання 21 задачі з перевірками Скрам-майстра.
6. **[Протокол підсумкової ретроспективи (docs/sprint2/retro-sprint2.md)](docs/sprint2/retro-sprint2.md)** — аналіз у форматі *Start / Stop / Continue*.
7. **[Повний звіт та навігація Спринту 2 (docs/sprint2/README.md)](docs/sprint2/README.md)**.

### 🔍 Первинні аркуші аудиту (правило 2 аудиторів на розділ):
- [Аркуш Олександра Панайотова (Lead)](docs/sprint2/audit/independent-audit-sheet-panaiotov.md)
- [Аркуш Дениса Бучинського (TA)](docs/sprint2/audit/independent-audit-sheet-buchynskyi.md)
- [Аркуш Володимира Костенка (TE)](docs/sprint2/audit/independent-audit-sheet-kostenko.md)
- [Аркуш Олександра Пархоменка (Опонент)](docs/sprint2/audit/independent-audit-sheet-parkhomenko.md)
- [Аркуш Олександра Кираківського (SM)](docs/sprint2/audit/independent-audit-sheet-kyrakivskyi.md)

### 🛠️ Рольові документи та аналітика якості:
- [Опонування та спірні дефекти](docs/sprint2/roles/disputed-defects.md)
- [5 контрольних питань Замовника до Демо](docs/sprint2/roles/customer-questions-and-counterarguments.md)
- [Чек-лист верифікації реалістичності тест-плану](docs/sprint2/roles/test-plan-verification-checklist.md)
- [Акт затвердження Реєстру та пріоритетів тестування](docs/sprint2/roles/lead-approval-and-priorities.md)
- [Матриця розподілу розділів SRS 1.0](docs/sprint2/roles/srs-section-allocation.md)
- [Журнал технічних уточнень із Замовником](docs/sprint2/roles/customer-clarifications.md)
- [Архітектурна матриця рівнів і типів тестування](docs/sprint2/roles/test-levels-and-types-matrix.md)


---

## 📦 Спринт 1: «Продукт і вимоги» (Архів інкременту)

- **[Презентація захисту Спринту 1](docs/sprint1/EDUPORTAL_Sprint1_Presentation.pptx)**
- **[Характеристика продукту (docs/sprint1/product.md)](docs/sprint1/product.md)**
- **[Беклог User Stories (docs/sprint1/backlog.md)](docs/sprint1/backlog.md)**
- **[Таблиця задач та тайм-трекінг (docs/sprint1/tasks.md)](docs/sprint1/tasks.md)**
- **[Протокол ретроспективи (docs/sprint1/retro.md)](docs/sprint1/retro.md)**
- **Моделі BPMN 2.0:** [01_testing.bpmn](docs/sprint1/bpmn/01_testing.bpmn) ([SVG](docs/sprint1/bpmn/01_testing.svg)) | [02_course_creation.bpmn](docs/sprint1/bpmn/02_course_creation.bpmn) ([SVG](docs/sprint1/bpmn/02_course_creation.svg))

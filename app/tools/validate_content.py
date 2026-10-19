import json
import sys
from collections import Counter
from pathlib import Path

CONTENT_FILE = Path(
    "shared/src/commonMain/composeResources/files/content.json"
)

EXPECTED_COUNTS = {
    "tasks": 300,
    "penalties": 50,
    "totems": 8,
    "toys": 10,
    "categories": 24,
}

ALLOWED_GENDERS = {
    "any",
    "male",
    "female",
}

ALLOWED_LEVELS = {
    1,
    2,
    3,
    4,
    5,
}

ALLOWED_MODES = {
    2,
    3,
}

errors = []
warnings = []


def error(message):
    errors.append(message)


def warning(message):
    warnings.append(message)


def require_text(item, field, item_name):
    value = item.get(field)

    if not isinstance(value, str) or not value.strip():
        error(f"{item_name}: missing or empty {field}")


def check_unique_ids(items, field, section):
    values = []

    for index, item in enumerate(items):
        value = item.get(field)

        if not value:
            error(f"{section}[{index}]: missing {field}")
            continue

        values.append(value)

    duplicates = [
        value
        for value, count in Counter(values).items()
        if count > 1
    ]

    for value in duplicates:
        error(f"{section}: duplicate {field}: {value}")


def check_gender(value, item_name, field, allow_none=False):
    if value is None and allow_none:
        return

    if value not in ALLOWED_GENDERS:
        error(
            f"{item_name}: invalid {field}: {value!r}"
        )


def check_toy_ids(
    value,
    item_name,
    known_toy_ids
):
    if value is None:
        return

    if not isinstance(value, list):
        error(f"{item_name}: toy_ids must be a list or null")
        return

    if len(value) != len(set(value)):
        error(f"{item_name}: duplicate values in toy_ids")

    for toy_id in value:
        if toy_id not in known_toy_ids:
            error(
                f"{item_name}: unknown toy_id: {toy_id}"
            )


if not CONTENT_FILE.exists():
    print(f"ERROR: file not found: {CONTENT_FILE}")
    sys.exit(1)

with CONTENT_FILE.open(
    "r",
    encoding="utf-8"
) as file:
    data = json.load(file)

for section, expected_count in EXPECTED_COUNTS.items():
    items = data.get(section)

    if not isinstance(items, list):
        error(f"{section}: section is missing or is not a list")
        continue

    if len(items) != expected_count:
        error(
            f"{section}: expected {expected_count}, "
            f"found {len(items)}"
        )

tasks = data.get("tasks", [])
penalties = data.get("penalties", [])
totems = data.get("totems", [])
toys = data.get("toys", [])
categories = data.get("categories", [])

check_unique_ids(tasks, "task_id", "tasks")
check_unique_ids(penalties, "penalty_id", "penalties")
check_unique_ids(totems, "totem_id", "totems")
check_unique_ids(toys, "toy_id", "toys")
check_unique_ids(categories, "category_id", "categories")

category_ids = {
    item.get("category_id")
    for item in categories
    if item.get("category_id")
}

toy_ids = {
    item.get("toy_id")
    for item in toys
    if item.get("toy_id")
}

task_texts = Counter()
penalty_texts = Counter()

for task in tasks:
    task_id = task.get("task_id", "<unknown>")
    item_name = f"task {task_id}"

    level = task.get("level")
    mode = task.get("mode")
    participant_count = task.get("participant_count")
    duration = task.get("duration_seconds")

    if level not in ALLOWED_LEVELS:
        error(f"{item_name}: invalid level: {level}")

    if mode not in ALLOWED_MODES:
        error(f"{item_name}: invalid mode: {mode}")

    if not isinstance(participant_count, int):
        error(f"{item_name}: invalid participant_count")
    elif mode in ALLOWED_MODES and not (
        2 <= participant_count <= mode
    ):
        error(
            f"{item_name}: participant_count "
            f"{participant_count} is incompatible with mode {mode}"
        )

    check_gender(
        task.get("active_gender"),
        item_name,
        "active_gender"
    )
    check_gender(
        task.get("partner_1_gender"),
        item_name,
        "partner_1_gender"
    )
    check_gender(
        task.get("partner_2_gender"),
        item_name,
        "partner_2_gender",
        allow_none=True
    )

    require_text(task, "active_role", item_name)
    require_text(task, "partner_1_role", item_name)
    require_text(task, "text_en", item_name)

    if participant_count == 3:
        require_text(task, "partner_2_role", item_name)

        if task.get("partner_2_gender") is None:
            error(
                f"{item_name}: partner_2_gender is required "
                f"when participant_count is 3"
            )

    if task.get("category_id") not in category_ids:
        error(
            f"{item_name}: unknown category_id: "
            f"{task.get('category_id')}"
        )

    check_toy_ids(
        task.get("toy_ids"),
        item_name,
        toy_ids
    )

    if duration is not None and (
        not isinstance(duration, int) or duration <= 0
    ):
        error(
            f"{item_name}: duration_seconds must be "
            f"a positive integer or null"
        )

    if not isinstance(task.get("enabled"), bool):
        error(f"{item_name}: enabled must be true or false")

    text = task.get("text_en")

    if isinstance(text, str):
        task_texts[text.strip().lower()] += 1

for penalty in penalties:
    penalty_id = penalty.get(
        "penalty_id",
        "<unknown>"
    )
    item_name = f"penalty {penalty_id}"

    level = penalty.get("intensity_level")
    mode = penalty.get("mode")
    recipient_count = penalty.get("recipient_count")
    duration = penalty.get("duration_seconds")

    if level not in ALLOWED_LEVELS:
        error(
            f"{item_name}: invalid intensity_level: {level}"
        )

    if mode not in ALLOWED_MODES:
        error(f"{item_name}: invalid mode: {mode}")

    if not isinstance(recipient_count, int):
        error(f"{item_name}: invalid recipient_count")
    elif recipient_count not in {1, 2}:
        error(
            f"{item_name}: recipient_count must be 1 or 2"
        )

    if mode == 2 and recipient_count != 1:
        error(
            f"{item_name}: mode 2 must have "
            f"recipient_count 1"
        )

    check_gender(
        penalty.get("active_gender"),
        item_name,
        "active_gender"
    )
    check_gender(
        penalty.get("partner_1_gender"),
        item_name,
        "partner_1_gender"
    )
    check_gender(
        penalty.get("partner_2_gender"),
        item_name,
        "partner_2_gender",
        allow_none=True
    )

    require_text(penalty, "text_en", item_name)

    if penalty.get("category_id") not in category_ids:
        error(
            f"{item_name}: unknown category_id: "
            f"{penalty.get('category_id')}"
        )

    check_toy_ids(
        penalty.get("toy_ids"),
        item_name,
        toy_ids
    )

    if duration is not None and (
        not isinstance(duration, int) or duration <= 0
    ):
        error(
            f"{item_name}: duration_seconds must be "
            f"a positive integer or null"
        )

    if not isinstance(penalty.get("enabled"), bool):
        error(f"{item_name}: enabled must be true or false")

    text = penalty.get("text_en")

    if isinstance(text, str):
        penalty_texts[text.strip().lower()] += 1

for totem in totems:
    totem_id = totem.get("totem_id", "<unknown>")
    item_name = f"totem {totem_id}"

    require_text(totem, "name_male_en", item_name)
    require_text(totem, "name_female_en", item_name)
    require_text(totem, "name_neutral_en", item_name)
    require_text(totem, "image_asset", item_name)

    if not isinstance(totem.get("enabled"), bool):
        error(f"{item_name}: enabled must be true or false")

for toy in toys:
    toy_id = toy.get("toy_id", "<unknown>")
    item_name = f"toy {toy_id}"

    require_text(toy, "name_en", item_name)
    require_text(toy, "image_asset", item_name)

    if toy.get("category_id") not in category_ids:
        error(
            f"{item_name}: unknown category_id: "
            f"{toy.get('category_id')}"
        )

    if not isinstance(toy.get("penetrative"), bool):
        error(f"{item_name}: penetrative must be true or false")

    if not isinstance(toy.get("enabled"), bool):
        error(f"{item_name}: enabled must be true or false")

for category in categories:
    category_id = category.get(
        "category_id",
        "<unknown>"
    )
    item_name = f"category {category_id}"

    require_text(category, "name_en", item_name)

    if category.get("content_type") not in {
        "task",
        "penalty",
        "both",
    }:
        error(
            f"{item_name}: invalid content_type: "
            f"{category.get('content_type')}"
        )

    if not isinstance(category.get("enabled"), bool):
        error(f"{item_name}: enabled must be true or false")

task_levels = Counter(
    task.get("level")
    for task in tasks
)

task_modes = Counter(
    task.get("mode")
    for task in tasks
)

penalty_levels = Counter(
    penalty.get("intensity_level")
    for penalty in penalties
)

for level in ALLOWED_LEVELS:
    if task_levels[level] != 60:
        error(
            f"tasks: level {level} must contain 60 tasks, "
            f"found {task_levels[level]}"
        )

if task_modes[2] != 150:
    error(
        f"tasks: mode 2 must contain 150 tasks, "
        f"found {task_modes[2]}"
    )

if task_modes[3] != 150:
    error(
        f"tasks: mode 3 must contain 150 tasks, "
        f"found {task_modes[3]}"
    )

for text, count in task_texts.items():
    if count > 1:
        warning(
            f"duplicate task text used {count} times: {text}"
        )

for text, count in penalty_texts.items():
    if count > 1:
        warning(
            f"duplicate penalty text used {count} times: {text}"
        )

print()
print("CONTENT DATABASE VALIDATION")
print("===========================")
print(f"Tasks:     {len(tasks)}")
print(f"Penalties: {len(penalties)}")
print(f"Totems:    {len(totems)}")
print(f"Toys:      {len(toys)}")
print(f"Categories:{len(categories)}")
print()

if warnings:
    print(f"WARNINGS: {len(warnings)}")

    for message in warnings:
        print(f"WARNING: {message}")

    print()

if errors:
    print(f"ERRORS: {len(errors)}")

    for message in errors:
        print(f"ERROR: {message}")

    sys.exit(1)

print("RESULT: OK")
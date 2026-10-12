# Automated Project Task Planning and Scheduling

## Project Description

A **terminal-based Java application** that models project task planning using a
**Directed Graph** and the **Critical Path Method (CPM)**.
It lets you define tasks, wire up dependencies, detect cycles, and automatically
compute the optimal execution schedule — all from a simple numbered menu.

---

## Objectives

1. Model real-world project schedules as a **Directed Acyclic Graph (DAG)**
2. Detect circular dependencies using **Kahn's Algorithm**
3. Generate a valid execution order via **Topological Sort**
4. Compute **Earliest/Latest Start and Finish** times (CPM)
5. Identify **critical tasks** and the **Critical Path**
6. Calculate the minimum **project completion time**

---

## Features

| # | Feature |
|---|---------|
| 1 | Create a named project |
| 2 | Add tasks (ID, name, duration) |
| 3 | Add task dependencies |
| 4 | View all tasks in a formatted table |
| 5 | View all dependencies |
| 6 | Generate schedule (cycle check + topo sort + CPM) |
| 7 | View Critical Path with arrow diagram |
| 8 | View Project Summary |
| 9 | Load a ready-made demo project |
| 0 | Exit |

---

## DSA Concepts Used

| Concept | Where Used |
|---------|------------|
| Directed Graph | Task dependency network |
| Adjacency List | HashMap of String to List of String in Graph.java |
| In-degree Array | HashMap of String to Integer for Kahn's algorithm |
| Queue (LinkedList) | BFS queue in Kahn's algorithm |
| HashSet | Storing unique task IDs |
| HashMap | O(1) task lookup by ID |
| ArrayList | Ordered task and path lists |
| Topological Sort | Kahn's BFS algorithm |
| Cycle Detection | Kahn's processed-count check |
| CPM Forward Pass | Earliest Start/Finish times |
| CPM Backward Pass | Latest Start/Finish times |
| Slack Calculation | Identify critical tasks |

---

## Algorithm Explanation

### 1. Kahn's Algorithm (Cycle Detection + Topological Sort)

```
1. Compute in-degree for every node (task).
2. Add all nodes with in-degree = 0 to a queue.
3. While the queue is not empty:
     a. Dequeue a node u.
     b. Add u to the topological order.
     c. For each neighbor v of u:
          - Decrement in-degree[v]
          - If in-degree[v] == 0, enqueue v
4. If processed nodes < total nodes  -->  CYCLE DETECTED
   Else  -->  topological order is valid.

Time  Complexity: O(V + E)   V = tasks, E = dependencies
Space Complexity: O(V)
```

### 2. CPM Forward Pass

```
For each task in topological order:
  EST(task) = max(EFT of all predecessors)   [0 if no predecessors]
  EFT(task) = EST(task) + duration
```

### 3. CPM Backward Pass

```
Project Duration = max(EFT of all tasks)
For each task in REVERSE topological order:
  LFT(task) = min(LST of all successors)     [Project Duration if terminal]
  LST(task) = LFT(task) - duration
```

### 4. Slack and Critical Path

```
Slack(task)   = LST - EST
Critical task  when  Slack == 0
Critical Path = longest chain of critical tasks through the graph
```

---

## Class Structure

```
proj(DSA)/
|
+-- src/
|   +-- Main.java            <- Entry point, menu loop
|   +-- Task.java            <- Task entity (id, name, duration, timing fields)
|   +-- Dependency.java      <- Directed edge (fromTaskId to toTaskId)
|   +-- Project.java         <- Stores all tasks and dependencies
|   +-- Graph.java           <- Directed graph: adjacency list, Kahn's algorithm
|   +-- Scheduler.java       <- CPM: forward pass, backward pass, slack
|   +-- CriticalPath.java    <- Identifies critical tasks, builds the path
|   +-- ProjectManager.java  <- Controller: handles each menu option
|   +-- InputValidator.java  <- Input validation utilities
|   +-- DisplayUtil.java     <- All terminal formatting / print methods
|
+-- README.md
```

### Responsibility Summary

| Class | Responsibility |
|-------|---------------|
| Main.java | Menu loop, reads user input, delegates to ProjectManager |
| Task.java | Holds task data and CPM timing fields |
| Dependency.java | Represents one directed edge |
| Project.java | Central data store: HashMap of taskId to Task + dependency list |
| Graph.java | Builds adjacency list, detects cycles, topological sort |
| Scheduler.java | Forward/backward pass, computes slack, marks critical tasks |
| CriticalPath.java | Collects critical tasks, traces the critical path |
| ProjectManager.java | Business logic for every menu action |
| InputValidator.java | Validates IDs, names, durations |
| DisplayUtil.java | Clean terminal output, tables, banners |

---

## How to Compile

Open a terminal in the project root (proj(DSA)/) and run:

```bash
javac src/*.java
```

Requirements: Java 17 or later
Check version: java -version

---

## How to Run

```bash
java src.Main
```

---

## Sample Input / Output

### Demo Project (Option 9 then Option 6)

Input typed at menu:
```
9      <- Load demo project
6      <- Generate schedule
7      <- View critical path
8      <- View project summary
0      <- Exit
```

Schedule Table output:
```
========================================
  PROJECT SCHEDULE
========================================
  (* = Critical Task)

  TASK                       START  FINISH  DURATION  SLACK
  ----------------------------------------
  Requirement Analysis           0       3    3 days      0 *
  System Design                  3       7    4 days      0 *
  Backend Development            7      12    5 days      0 *
  Frontend Development           7      11    4 days      4
  Database Integration          12      15    3 days      0 *
  Testing                       15      18    3 days      0 *
  Deployment                    18      20    2 days      0 *
  ----------------------------------------
```

Critical Path output:
```
========================================
  CRITICAL PATH ANALYSIS
========================================
  Critical Tasks:
  ----------------------------------------
  T1       Requirement Analysis
  T2       System Design
  T3       Backend Development
  T5       Database Integration
  T6       Testing
  T7       Deployment

  Critical Path:
  ----------------------------------------
  Requirement Analysis  (3 days)
          |
  System Design  (4 days)
          |
  Backend Development  (5 days)
          |
  Database Integration  (3 days)
          |
  Testing  (3 days)
          |
  Deployment  (2 days)

  Path: T1 -> T2 -> T3 -> T5 -> T6 -> T7
  ----------------------------------------
  Project Duration: 20 days
```

---

## Test Cases

### Test 1 — Demo Project (No Cycle, expected values)

| Task | Duration | EST | EFT | LST | LFT | Slack | Critical? |
|------|----------|-----|-----|-----|-----|-------|-----------|
| T1   | 3        | 0   | 3   | 0   | 3   | 0     | Yes |
| T2   | 4        | 3   | 7   | 3   | 7   | 0     | Yes |
| T3   | 5        | 7   | 12  | 7   | 12  | 0     | Yes |
| T4   | 4        | 7   | 11  | 11  | 15  | 4     | No  |
| T5   | 3        | 12  | 15  | 12  | 15  | 0     | Yes |
| T6   | 3        | 15  | 18  | 15  | 18  | 0     | Yes |
| T7   | 2        | 18  | 20  | 18  | 20  | 0     | Yes |

Project Duration: 20 days

---

### Test 2 — Cycle Detection

Add dependencies: T1 to T2, T2 to T3, T3 to T1

Expected output:
```
========================================
  ERROR
========================================
  Circular dependency detected!
  Schedule CANNOT be generated.
```

---

### Test 3 — Duplicate Task ID

Try adding Task ID T1 twice.

Expected output:
```
[ERR] Task ID 'T1' already exists. Use a unique ID.
```

---

### Test 4 — Invalid Duration

Enter duration 0 or -5.

Expected output:
```
[ERR] Duration must be a positive integer (greater than 0).
```

---

### Test 5 — Generate Schedule Before Adding Tasks

Select Option 6 immediately after starting.

Expected output:
```
[ERR] No tasks found. Please add tasks first (Option 2).
```

---

## Time Complexity

| Operation | Complexity | Reason |
|-----------|-----------|--------|
| Add Task | O(1) | HashMap insert |
| Add Dependency | O(D) | Duplicate scan over D existing deps |
| Cycle Detection | O(V + E) | Kahn's BFS |
| Topological Sort | O(V + E) | Kahn's BFS |
| Forward Pass (CPM) | O(V + E) | One pass over topo order |
| Backward Pass (CPM) | O(V + E) | One reverse pass |
| Slack Calculation | O(V) | One pass over all tasks |
| Critical Path Trace | O(V + E) | Graph traversal |
| Total | O(V + E) | Linear in tasks + dependencies |

---

## Space Complexity

| Structure | Space |
|-----------|-------|
| Adjacency List | O(V + E) |
| In-degree Map | O(V) |
| Task Map | O(V) |
| Queue (Kahn's) | O(V) |
| Topo Order List | O(V) |
| Total | O(V + E) |

---

## Viva Demonstration Guide

Recommended demonstration order:

1. Start  ->  Run: java src.Main
2. Option 9  ->  Load Demo Project (shows pre-loaded data instantly)
3. Option 4  ->  View Tasks table
4. Option 5  ->  View Dependencies (show the edges)
5. Option 6  ->  Generate Schedule
   - Point out: The system first checks for cycles using Kahn's Algorithm
   - Point out: Then it performs Topological Sort
   - Point out: Then Forward and Backward Pass for CPM
6. Option 7  ->  View Critical Path
   - Explain: T4 (Frontend) has slack 4 — it can be delayed 4 days without affecting the deadline
   - Explain: The critical path T1->T2->T3->T5->T6->T7 takes exactly 20 days
7. Option 8  ->  Project Summary
8. Option 1  ->  Create new project, manually add tasks and deps to show real input flow
9. Cycle Test  ->  Add T1->T2, T2->T3, T3->T1 then run Option 6 to show cycle error

Key viva points to explain:

- Why Kahn's algorithm detects cycles:
  "If not all nodes are processed, some are stuck waiting — that proves a cycle exists"

- Why slack = 0 means critical:
  "The task has zero float — any delay directly extends the project deadline"

- Why we need the backward pass:
  "Forward pass gives earliest possible times; backward pass gives latest allowable times.
   The difference is the slack."

- Data structure choice:
  "HashMap gives O(1) lookup; ArrayList preserves insertion order;
   LinkedList as Queue gives O(1) enqueue/dequeue for Kahn's BFS"

---

Built with standard Java 17. No external libraries required.

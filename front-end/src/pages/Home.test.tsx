import { describe, test, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import "@testing-library/jest-dom";

import Home from "./Home";
import * as taskServices from "../services/taskServices";
import type { Task } from "../types";

// mock the taskServices module used inside useTasks
vi.mock("../services/taskServices", () => ({
    fetchRecentTasks: vi.fn(),
    createTask: vi.fn(),
    completeTask: vi.fn(),
}));

describe("Home", () => {
    const sampleTasks: Task[] = [
        {
            id: 1,
            taskId: "T-0001",
            title: "Task 1",
            description: "First task",
            completed: false,
            createdAt: "2024-01-01T00:00:00Z",
        },
        {
            id: 2,
            taskId: "T-0002",
            title: "Task 2",
            description: "Second task",
            completed: false,
            createdAt: "2024-01-02T00:00:00Z",
        },
    ];

    const mockedFetchRecentTasks = taskServices.fetchRecentTasks as Mock;
    const mockedCompleteTask = taskServices.completeTask as Mock;
    const mockedCreateTask = taskServices.createTask as Mock;


    beforeEach(() => {
        vi.clearAllMocks();
    });

    test("loads and displays tasks on mount", async () => {
        mockedFetchRecentTasks.mockResolvedValueOnce(sampleTasks);

        render(<Home />);

        // shows loading state first
        expect(screen.getByText(/loading tasks/i)).toBeInTheDocument();

        // waits for tasks to appear after useTasks finishes loading
        await waitFor(() => {
            expect(screen.getByText("Task 1")).toBeInTheDocument();
            expect(screen.getByText("Task 2")).toBeInTheDocument();
        });

        expect(mockedFetchRecentTasks).toHaveBeenCalledTimes(1);
    });

    test('clicking "Done" hides a task (after refetch)', async () => {
        // initial load
        mockedFetchRecentTasks.mockResolvedValueOnce(sampleTasks);

        // after completing a task, backend returns updated list
        mockedFetchRecentTasks.mockResolvedValueOnce([
            {
                id: 2,
                taskId: "T-0002",
                title: "Task 2",
                description: "Second task",
                completed: false,
                createdAt: "2024-01-02T00:00:00Z",
            },
        ]);

        mockedCompleteTask.mockResolvedValueOnce(undefined);

        render(<Home />);

        // wait until tasks are rendered
        await waitFor(() => {
            expect(screen.getByText("Task 1")).toBeInTheDocument();
            expect(screen.getByText("Task 2")).toBeInTheDocument();
        });

        const doneButtons = screen.getAllByRole("button", { name: /done/i });
        const firstDoneButton = doneButtons[0];

        // click "Done" on Task 1
        fireEvent.click(firstDoneButton);

        // UI should update according to 2nd fetchRecentTasks result
        await waitFor(() => {
            expect(screen.queryByText("Task 1")).not.toBeInTheDocument();
        });

        // other task still there
        expect(screen.getByText("Task 2")).toBeInTheDocument();

        // completeTask API called with correct id
        expect(mockedCompleteTask).toHaveBeenCalledTimes(1);
        expect(mockedCompleteTask).toHaveBeenCalledWith(1);

        // fetchRecentTasks was called twice (initial + reload)
        expect(mockedFetchRecentTasks).toHaveBeenCalledTimes(2);
    });

    test("adding a task triggers refetch and updates the UI", async () => {
        // Initial load , empty list
        mockedFetchRecentTasks.mockResolvedValueOnce([]);

        // createTask returns successfully
        mockedCreateTask.mockResolvedValueOnce(undefined);

        // After adding, backend returns new updated list
        mockedFetchRecentTasks.mockResolvedValueOnce([
            {
                id: 10,
                taskId: "T-0010",
                title: "New Task",
                description: "Created via test",
                completed: false,
                createdAt: "2024-01-03T00:00:00Z",
            },
        ]);

        render(<Home />);

        // Ensure no tasks initially
        await waitFor(() => {
            expect(screen.queryByText("New Task")).not.toBeInTheDocument();
        });

        // Fill in Add Task form
        fireEvent.change(screen.getByLabelText(/task title/i), {
            target: { value: "New Task" },
        });

        fireEvent.change(screen.getByLabelText(/description/i), {
            target: { value: "Created via test" },
        });

        // Submit
        fireEvent.click(screen.getByRole("button", { name: /add task/i }));

        // After refetch, new task should appear
        await waitFor(() => {
            expect(screen.getByText("New Task")).toBeInTheDocument();
        });

        // createTask API called with payload
        expect(mockedCreateTask).toHaveBeenCalledTimes(1);
        expect(mockedCreateTask).toHaveBeenCalledWith({
            title: "New Task",
            description: "Created via test",
        });

        // fetchRecentTasks should now have been called twice
        expect(mockedFetchRecentTasks).toHaveBeenCalledTimes(2);
    });
});

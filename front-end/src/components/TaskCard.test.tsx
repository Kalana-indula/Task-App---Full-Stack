import { describe, test, expect, vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import TaskCard from "./TaskCard";
import type { Task } from "../types";

describe("TaskCard", () => {
    const sampleTask: Task = {
        id: 1,
        taskId: "T-0001",
        title: "Sample Task",
        description: "Sample description",
        completed: false,
        createdAt: "2024-01-01T00:00:00Z",
    };

    test("renders task title and description", () => {
        const onCompleted = vi.fn();

        render(<TaskCard task={sampleTask} onCompleted={onCompleted} />);

        expect(screen.getByText("Sample Task")).toBeInTheDocument();
        expect(screen.getByText("Sample description")).toBeInTheDocument();
    });

    test('clicking "Done" calls onCompleted with task id', () => {
        const onCompleted = vi.fn();

        render(<TaskCard task={sampleTask} onCompleted={onCompleted} />);

        const button = screen.getByRole("button", { name: /done/i });

        fireEvent.click(button);

        expect(onCompleted).toHaveBeenCalledTimes(1);
        expect(onCompleted).toHaveBeenCalledWith(1);
    });
});

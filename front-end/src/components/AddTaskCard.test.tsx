import { describe, test, expect, vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import AddTaskCard from "./AddTaskCard";

describe("AddTaskCard", () => {
    test("renders title and description inputs", () => {
        const dummyOnSubmit = vi.fn();

        render(<AddTaskCard onSubmit={dummyOnSubmit} />);

        const titleInput = screen.getByLabelText(/task title/i);
        const descriptionTextarea = screen.getByLabelText(/description/i);
        const button = screen.getByRole("button", { name: /add task/i });

        expect(titleInput).toBeInTheDocument();
        expect(descriptionTextarea).toBeInTheDocument();
        expect(button).toBeInTheDocument();
    });

    test("calls onSubmit with correct values", () => {
        const handleSubmit = vi.fn();

        render(<AddTaskCard onSubmit={handleSubmit} />);

        const titleInput = screen.getByLabelText(/task title/i);
        const descriptionTextarea = screen.getByLabelText(/description/i);
        const button = screen.getByRole("button", { name: /add task/i });

        // type into inputs
        fireEvent.change(titleInput, { target: { value: "Test task" } });
        fireEvent.change(descriptionTextarea, {
            target: { value: "This is a test description" },
        });

        // click submit
        fireEvent.click(button);

        expect(handleSubmit).toHaveBeenCalledTimes(1);
        expect(handleSubmit).toHaveBeenCalledWith({
            title: "Test task",
            description: "This is a test description",
        });
    });
});

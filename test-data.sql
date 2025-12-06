-- Test Data SQL Script for Software Submission Backend
-- This script creates dummy data for testing purposes
--
-- NOTE: Submission uniqueness constraint: One submission per student per problem per contest
-- The same student can have different submissions for the same problem in different contests

-- Clear existing data (in reverse order of dependencies)
DELETE FROM leader_board_entry;
DELETE FROM submission;
DELETE FROM test_case;
DELETE FROM contest_problems;
DELETE FROM contest_participants;
DELETE FROM problem;
DELETE FROM contest;
DELETE FROM users;

-- Insert Users
INSERT INTO users (id, first_name, last_name, email, password, email_verified, failed_login_attempts, account_locked, role, created_at, updated_at) VALUES
-- Teachers
('550e8400-e29b-41d4-a716-446655440001', 'John', 'Smith', 'teacher@gmail.com', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', true, 0, false, 'TEACHER', '2024-01-15 10:00:00+00:00', '2024-01-15 10:00:00+00:00'),
('550e8400-e29b-41d4-a716-446655440002', 'Sarah', 'Johnson', 'sarah.johnson@university.edu', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', true, 0, false, 'TEACHER', '2024-01-20 09:30:00+00:00', '2024-01-20 09:30:00+00:00'),
('550e8400-e29b-41d4-a716-446655440003', 'Michael', 'Davis', 'michael.davis@university.edu', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', true, 0, false, 'TEACHER', '2024-02-01 11:15:00+00:00', '2024-02-01 11:15:00+00:00'),

-- Students
('550e8400-e29b-41d4-a716-446655440011', 'Alice', 'Wilson', 'student@gmail.com', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', true, 0, false, 'STUDENT', '2024-01-10 14:30:00+00:00', '2024-01-10 14:30:00+00:00'),
('550e8400-e29b-41d4-a716-446655440012', 'Bob', 'Brown', 'bob.brown@student.edu', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', true, 0, false, 'STUDENT', '2024-01-12 16:45:00+00:00', '2024-01-12 16:45:00+00:00'),
('550e8400-e29b-41d4-a716-446655440013', 'Charlie', 'Garcia', 'charlie.garcia@student.edu', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', true, 0, false, 'STUDENT', '2024-01-18 13:20:00+00:00', '2024-01-18 13:20:00+00:00'),
('550e8400-e29b-41d4-a716-446655440014', 'Diana', 'Martinez', 'diana.martinez@student.edu', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', true, 0, false, 'STUDENT', '2024-01-25 10:10:00+00:00', '2024-01-25 10:10:00+00:00'),
('550e8400-e29b-41d4-a716-446655440015', 'Eva', 'Anderson', 'eva.anderson@student.edu', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', true, 0, false, 'STUDENT', '2024-02-05 15:25:00+00:00', '2024-02-05 15:25:00+00:00'),
('550e8400-e29b-41d4-a716-446655440016', 'Frank', 'Taylor', 'frank.taylor@student.edu', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', true, 0, false, 'STUDENT', '2024-02-10 12:40:00+00:00', '2024-02-10 12:40:00+00:00');

-- Insert Problems
INSERT INTO problem (id, title, statement, author_id, difficulty_level, created_at, updated_at) VALUES
('660e8400-e29b-41d4-a716-446655440001', 'Two Sum', 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target. You may assume that each input would have exactly one solution, and you may not use the same element twice.', '550e8400-e29b-41d4-a716-446655440001', 'EASY', '2024-03-01 09:00:00+00:00', '2024-03-01 09:00:00+00:00'),
('660e8400-e29b-41d4-a716-446655440002', 'Reverse Integer', 'Given a signed 32-bit integer x, return x with its digits reversed. If reversing x causes the value to go outside the signed 32-bit integer range [-2^31, 2^31 - 1], then return 0.', '550e8400-e29b-41d4-a716-446655440001', 'EASY', '2024-03-02 10:15:00+00:00', '2024-03-02 10:15:00+00:00'),
('660e8400-e29b-41d4-a716-446655440003', 'Longest Substring Without Repeating Characters', 'Given a string s, find the length of the longest substring without repeating characters.', '550e8400-e29b-41d4-a716-446655440002', 'MEDIUM', '2024-03-03 11:30:00+00:00', '2024-03-03 11:30:00+00:00'),
('660e8400-e29b-41d4-a716-446655440004', 'Median of Two Sorted Arrays', 'Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.', '550e8400-e29b-41d4-a716-446655440002', 'HARD', '2024-03-04 13:45:00+00:00', '2024-03-04 13:45:00+00:00'),
('660e8400-e29b-41d4-a716-446655440005', 'Palindrome Number', 'Given an integer x, return true if x is a palindrome, and false otherwise.', '550e8400-e29b-41d4-a716-446655440003', 'EASY', '2024-03-05 14:20:00+00:00', '2024-03-05 14:20:00+00:00'),
('660e8400-e29b-41d4-a716-446655440006', 'Valid Parentheses', 'Given a string s containing just the characters ''('', '')'', ''{'', ''}'', ''['' and '']'', determine if the input string is valid.', '550e8400-e29b-41d4-a716-446655440003', 'EASY', '2024-03-06 15:30:00+00:00', '2024-03-06 15:30:00+00:00');

-- Insert Test Cases for Problems
INSERT INTO test_case (id, input, expected_output, type, problem_id, created_at, updated_at) VALUES
-- Two Sum test cases
('770e8400-e29b-41d4-a716-446655440001', '[2,7,11,15], 9', '[0,1]', 'SAMPLE', '660e8400-e29b-41d4-a716-446655440001', '2024-03-01 09:05:00+00:00', '2024-03-01 09:05:00+00:00'),
('770e8400-e29b-41d4-a716-446655440002', '[3,2,4], 6', '[1,2]', 'SAMPLE', '660e8400-e29b-41d4-a716-446655440001', '2024-03-01 09:05:00+00:00', '2024-03-01 09:05:00+00:00'),
('770e8400-e29b-41d4-a716-446655440003', '[3,3], 6', '[0,1]', 'HIDDEN', '660e8400-e29b-41d4-a716-446655440001', '2024-03-01 09:05:00+00:00', '2024-03-01 09:05:00+00:00'),

-- Reverse Integer test cases
('770e8400-e29b-41d4-a716-446655440004', '123', '321', 'SAMPLE', '660e8400-e29b-41d4-a716-446655440002', '2024-03-02 10:20:00+00:00', '2024-03-02 10:20:00+00:00'),
('770e8400-e29b-41d4-a716-446655440005', '-123', '-321', 'SAMPLE', '660e8400-e29b-41d4-a716-446655440002', '2024-03-02 10:20:00+00:00', '2024-03-02 10:20:00+00:00'),
('770e8400-e29b-41d4-a716-446655440006', '120', '21', 'HIDDEN', '660e8400-e29b-41d4-a716-446655440002', '2024-03-02 10:20:00+00:00', '2024-03-02 10:20:00+00:00'),

-- Longest Substring test cases
('770e8400-e29b-41d4-a716-446655440007', 'abcabcbb', '3', 'SAMPLE', '660e8400-e29b-41d4-a716-446655440003', '2024-03-03 11:35:00+00:00', '2024-03-03 11:35:00+00:00'),
('770e8400-e29b-41d4-a716-446655440008', 'bbbbb', '1', 'SAMPLE', '660e8400-e29b-41d4-a716-446655440003', '2024-03-03 11:35:00+00:00', '2024-03-03 11:35:00+00:00'),
('770e8400-e29b-41d4-a716-446655440009', 'pwwkew', '3', 'HIDDEN', '660e8400-e29b-41d4-a716-446655440003', '2024-03-03 11:35:00+00:00', '2024-03-03 11:35:00+00:00'),

-- Palindrome Number test cases
('770e8400-e29b-41d4-a716-446655440010', '121', 'true', 'SAMPLE', '660e8400-e29b-41d4-a716-446655440005', '2024-03-05 14:25:00+00:00', '2024-03-05 14:25:00+00:00'),
('770e8400-e29b-41d4-a716-446655440011', '-121', 'false', 'SAMPLE', '660e8400-e29b-41d4-a716-446655440005', '2024-03-05 14:25:00+00:00', '2024-03-05 14:25:00+00:00'),
('770e8400-e29b-41d4-a716-446655440012', '10', 'false', 'HIDDEN', '660e8400-e29b-41d4-a716-446655440005', '2024-03-05 14:25:00+00:00', '2024-03-05 14:25:00+00:00'),

-- Valid Parentheses test cases
('770e8400-e29b-41d4-a716-446655440013', '()', 'true', 'SAMPLE', '660e8400-e29b-41d4-a716-446655440006', '2024-03-06 15:35:00+00:00', '2024-03-06 15:35:00+00:00'),
('770e8400-e29b-41d4-a716-446655440014', '()[]{}', 'true', 'SAMPLE', '660e8400-e29b-41d4-a716-446655440006', '2024-03-06 15:35:00+00:00', '2024-03-06 15:35:00+00:00'),
('770e8400-e29b-41d4-a716-446655440015', '(]', 'false', 'HIDDEN', '660e8400-e29b-41d4-a716-446655440006', '2024-03-06 15:35:00+00:00', '2024-03-06 15:35:00+00:00');

-- Insert Contests
INSERT INTO contest (id, name, description, enrollment_key, start_time, end_time, author_id, created_at, updated_at) VALUES
('880e8400-e29b-41d4-a716-446655440001', 'Beginner Programming Contest', 'A contest designed for programming beginners to practice basic algorithmic problems.', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', '2024-04-01 10:00:00+00:00', '2024-04-01 18:00:00+00:00', '550e8400-e29b-41d4-a716-446655440001', '2024-03-15 08:00:00+00:00', '2024-03-15 08:00:00+00:00'),
('880e8400-e29b-41d4-a716-446655440002', 'Data Structures Challenge', 'Advanced contest focusing on data structures and algorithms.', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', '2024-04-15 09:00:00+00:00', '2024-04-15 21:00:00+00:00', '550e8400-e29b-41d4-a716-446655440002', '2024-03-20 10:30:00+00:00', '2024-03-20 10:30:00+00:00'),
('880e8400-e29b-41d4-a716-446655440003', 'Weekly Practice Round', 'Weekly practice contest for continuous learning.', '$2a$10$8NFOu61K4o.3mFEJ/gZdGOW7/WcfC5lFC.sgQr9gwNTuomNLgX1xe', '2024-04-08 14:00:00+00:00', '2024-04-08 17:00:00+00:00', '550e8400-e29b-41d4-a716-446655440003', '2024-03-25 12:00:00+00:00', '2024-03-25 12:00:00+00:00');

-- Insert Contest-Problem relationships
INSERT INTO contest_problems (contest_id, problem_id) VALUES
-- Beginner Contest problems
('880e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440001'),
('880e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440002'),
('880e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440005'),
('880e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440006'),

-- Advanced Contest problems
('880e8400-e29b-41d4-a716-446655440002', '660e8400-e29b-41d4-a716-446655440003'),
('880e8400-e29b-41d4-a716-446655440002', '660e8400-e29b-41d4-a716-446655440004'),

-- Weekly Practice problems
('880e8400-e29b-41d4-a716-446655440003', '660e8400-e29b-41d4-a716-446655440001'),
('880e8400-e29b-41d4-a716-446655440003', '660e8400-e29b-41d4-a716-446655440003'),
('880e8400-e29b-41d4-a716-446655440003', '660e8400-e29b-41d4-a716-446655440005');

-- Insert Contest Participants
INSERT INTO contest_participants (user_id, contest_id) VALUES
-- Beginner Contest participants
('550e8400-e29b-41d4-a716-446655440011', '880e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655440012', '880e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655440013', '880e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655440014', '880e8400-e29b-41d4-a716-446655440001'),

-- Advanced Contest participants
('550e8400-e29b-41d4-a716-446655440013', '880e8400-e29b-41d4-a716-446655440002'),
('550e8400-e29b-41d4-a716-446655440014', '880e8400-e29b-41d4-a716-446655440002'),
('550e8400-e29b-41d4-a716-446655440015', '880e8400-e29b-41d4-a716-446655440002'),
('550e8400-e29b-41d4-a716-446655440016', '880e8400-e29b-41d4-a716-446655440002'),

-- Weekly Practice participants
('550e8400-e29b-41d4-a716-446655440011', '880e8400-e29b-41d4-a716-446655440003'),
('550e8400-e29b-41d4-a716-446655440012', '880e8400-e29b-41d4-a716-446655440003'),
('550e8400-e29b-41d4-a716-446655440015', '880e8400-e29b-41d4-a716-446655440003'),
('550e8400-e29b-41d4-a716-446655440016', '880e8400-e29b-41d4-a716-446655440003');

-- Insert Submissions (One submission per student per problem per contest)
INSERT INTO submission (id, code, submission_type, language, student_id, problem_id, contest_id, understanding_logic, correctness_score, readability_score, total_score, grading_result_status, created_at, updated_at) VALUES
-- Two Sum submission by Bob Brown in Beginner Contest
('990e8400-e29b-41d4-a716-446655440001', 'public int[] twoSum(int[] nums, int target) {\n    Map<Integer, Integer> map = new HashMap<>();\n    for (int i = 0; i < nums.length; i++) {\n        int complement = target - nums[i];\n        if (map.containsKey(complement)) {\n            return new int[] { map.get(complement), i };\n        }\n        map.put(nums[i], i);\n    }\n    return null;\n}', 'FINAL', 'Java', '550e8400-e29b-41d4-a716-446655440012', '660e8400-e29b-41d4-a716-446655440001', '880e8400-e29b-41d4-a716-446655440001', 90.0, 95.0, 85.0, 90.0, 'COMPLETED', '2024-04-01 12:15:00+00:00', '2024-04-01 12:20:00+00:00'),

-- Reverse Integer submission by Charlie Garcia in Beginner Contest
('990e8400-e29b-41d4-a716-446655440002', 'def reverse(self, x: int) -> int:\n    sign = -1 if x < 0 else 1\n    x = abs(x)\n    result = 0\n    while x:\n        result = result * 10 + x % 10\n        x //= 10\n    result *= sign\n    return result if -2**31 <= result <= 2**31 - 1 else 0', 'FINAL', 'Python', '550e8400-e29b-41d4-a716-446655440013', '660e8400-e29b-41d4-a716-446655440002', '880e8400-e29b-41d4-a716-446655440001', 88.0, 90.0, 82.0, 86.7, 'COMPLETED', '2024-04-01 13:20:00+00:00', '2024-04-01 13:25:00+00:00'),

-- Longest Substring submission by Eva Anderson in Advanced Contest
('990e8400-e29b-41d4-a716-446655440003', 'def lengthOfLongestSubstring(self, s: str) -> int:\n    char_map = {}\n    left = 0\n    max_length = 0\n    \n    for right in range(len(s)):\n        if s[right] in char_map and char_map[s[right]] >= left:\n            left = char_map[s[right]] + 1\n        char_map[s[right]] = right\n        max_length = max(max_length, right - left + 1)\n    \n    return max_length', 'FINAL', 'Python', '550e8400-e29b-41d4-a716-446655440015', '660e8400-e29b-41d4-a716-446655440003', '880e8400-e29b-41d4-a716-446655440002', 89.0, 93.0, 87.0, 89.7, 'COMPLETED', '2024-04-15 11:30:00+00:00', '2024-04-15 11:40:00+00:00'),

-- Palindrome Number submission by Alice Wilson in Beginner Contest
('990e8400-e29b-41d4-a716-446655440004', 'def isPalindrome(self, x: int) -> bool:\n    if x < 0 or (x != 0 and x % 10 == 0):\n        return False\n    \n    reversed_half = 0\n    while x > reversed_half:\n        reversed_half = reversed_half * 10 + x % 10\n        x //= 10\n    \n    return x == reversed_half or x == reversed_half // 10', 'FINAL', 'Python', '550e8400-e29b-41d4-a716-446655440011', '660e8400-e29b-41d4-a716-446655440005', '880e8400-e29b-41d4-a716-446655440001', 95.0, 98.0, 92.0, 95.0, 'COMPLETED', '2024-04-01 15:30:00+00:00', '2024-04-01 15:35:00+00:00'),

-- Valid Parentheses submission by Diana Martinez in Beginner Contest
('990e8400-e29b-41d4-a716-446655440005', 'def isValid(self, s: str) -> bool:\n    stack = []\n    mapping = {")": "(", "}": "{", "]": "["}\n    \n    for char in s:\n        if char in mapping:\n            top_element = stack.pop() if stack else "#"\n            if mapping[char] != top_element:\n                return False\n        else:\n            stack.append(char)\n    \n    return not stack', 'FINAL', 'Python', '550e8400-e29b-41d4-a716-446655440014', '660e8400-e29b-41d4-a716-446655440006', '880e8400-e29b-41d4-a716-446655440001', 92.0, 96.0, 88.0, 92.0, 'COMPLETED', '2024-04-01 16:45:00+00:00', '2024-04-01 16:50:00+00:00'),

-- Example: Same student, same problem, different contest (Alice Wilson - Two Sum in Weekly Practice)
('990e8400-e29b-41d4-a716-446655440006', 'def twoSum(self, nums, target):\n    for i in range(len(nums)):\n        for j in range(i + 1, len(nums)):\n            if nums[i] + nums[j] == target:\n                return [i, j]\n    return []', 'FINAL', 'Python', '550e8400-e29b-41d4-a716-446655440011', '660e8400-e29b-41d4-a716-446655440001', '880e8400-e29b-41d4-a716-446655440003', 70.0, 85.0, 75.0, 76.7, 'COMPLETED', '2024-04-08 15:20:00+00:00', '2024-04-08 15:25:00+00:00'),

-- Example: Same student, same problem, different contest (Bob Brown - Palindrome in Weekly Practice)
('990e8400-e29b-41d4-a716-446655440007', 'def isPalindrome(self, x: int) -> bool:\n    return str(x) == str(x)[::-1]', 'FINAL', 'Python', '550e8400-e29b-41d4-a716-446655440012', '660e8400-e29b-41d4-a716-446655440005', '880e8400-e29b-41d4-a716-446655440003', 80.0, 88.0, 85.0, 84.3, 'COMPLETED', '2024-04-08 16:10:00+00:00', '2024-04-08 16:15:00+00:00');

-- Insert Leaderboard Entries
INSERT INTO leader_board_entry (id, user_id, contest_id, total_score, rank, problems_solved, created_at, updated_at) VALUES
-- Beginner Contest Leaderboard
('aa0e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440011', '880e8400-e29b-41d4-a716-446655440001', 95.0, 1, 1, '2024-04-01 18:30:00+00:00', '2024-04-01 18:30:00+00:00'), -- Alice Wilson (Palindrome Number)
('aa0e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440014', '880e8400-e29b-41d4-a716-446655440001', 92.0, 2, 1, '2024-04-01 18:30:00+00:00', '2024-04-01 18:30:00+00:00'), -- Diana Martinez (Valid Parentheses)
('aa0e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440012', '880e8400-e29b-41d4-a716-446655440001', 90.0, 3, 1, '2024-04-01 18:30:00+00:00', '2024-04-01 18:30:00+00:00'), -- Bob Brown (Two Sum)
('aa0e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440013', '880e8400-e29b-41d4-a716-446655440001', 86.7, 4, 1, '2024-04-01 18:30:00+00:00', '2024-04-01 18:30:00+00:00'), -- Charlie Garcia (Reverse Integer)

-- Advanced Contest Leaderboard
('aa0e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440015', '880e8400-e29b-41d4-a716-446655440002', 89.7, 1, 1, '2024-04-15 21:30:00+00:00', '2024-04-15 21:30:00+00:00'),
('aa0e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440013', '880e8400-e29b-41d4-a716-446655440002', 0.0, 2, 0, '2024-04-15 21:30:00+00:00', '2024-04-15 21:30:00+00:00'),
('aa0e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440014', '880e8400-e29b-41d4-a716-446655440002', 0.0, 2, 0, '2024-04-15 21:30:00+00:00', '2024-04-15 21:30:00+00:00'),
('aa0e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440016', '880e8400-e29b-41d4-a716-446655440002', 0.0, 2, 0, '2024-04-15 21:30:00+00:00', '2024-04-15 21:30:00+00:00'),

-- Weekly Practice Leaderboard
('aa0e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440012', '880e8400-e29b-41d4-a716-446655440003', 84.3, 1, 1, '2024-04-08 17:30:00+00:00', '2024-04-08 17:30:00+00:00'), -- Bob Brown (Palindrome Number - different solution)
('aa0e8400-e29b-41d4-a716-446655440010', '550e8400-e29b-41d4-a716-446655440011', '880e8400-e29b-41d4-a716-446655440003', 76.7, 2, 1, '2024-04-08 17:30:00+00:00', '2024-04-08 17:30:00+00:00'), -- Alice Wilson (Two Sum - different solution)
('aa0e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440015', '880e8400-e29b-41d4-a716-446655440003', 0.0, 3, 0, '2024-04-08 17:30:00+00:00', '2024-04-08 17:30:00+00:00'), -- Eva Anderson (no submissions)
('aa0e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440016', '880e8400-e29b-41d4-a716-446655440003', 0.0, 3, 0, '2024-04-08 17:30:00+00:00', '2024-04-08 17:30:00+00:00'); -- Frank Taylor (no submissions)
